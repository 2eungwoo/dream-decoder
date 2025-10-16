package sideprojects.dreamdecoder.application.dream.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RStream;
import org.redisson.api.StreamMessageId;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.application.dream.service.DreamService;
import sideprojects.dreamdecoder.domain.dream.model.DreamModel;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.embedding.dto.EmbeddingSearchResponse;
import sideprojects.dreamdecoder.infrastructure.external.embedding.service.EmbeddingService;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.infrastructure.external.openai.service.DreamInterpretationGeneratorService;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.DreamSymbolExtractor;
import sideprojects.dreamdecoder.presentation.dream.dto.request.SaveDreamRequest;

import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DreamSaveJobProcessor {

    private final DreamService dreamService;
    private final DreamSymbolExtractor dreamSymbolExtractor;
    private final DreamInterpretationGeneratorService dreamInterpretationGeneratorService;
    private final EmbeddingService embeddingService;

    private record DreamJobPayload(Long userId, String dreamContent, DreamEmotion dreamEmotion, AiStyle style, String tags) {
        public static DreamJobPayload fromMessage(Map<String, String> messageBody) {
            return new DreamJobPayload(
                    Long.parseLong(messageBody.get("userId")),
                    messageBody.get("dreamContent"),
                    DreamEmotion.valueOf(messageBody.get("dreamEmotion")),
                    AiStyle.valueOf(messageBody.get("style")),
                    messageBody.get("tags")
            );
        }
    }

    public void process(RStream<String, String> stream, String groupName, StreamMessageId messageId,
                        Map<String, String> messageBody) {
        try {
            DreamJobPayload payload = DreamJobPayload.fromMessage(messageBody);

            // L2 벡터 캐시 확인 및 처리
            boolean handledByCache = handleVectorCacheHit(payload);
            if (handledByCache) {
                stream.ack(groupName, messageId);
                log.info("ACK 전송 (L2 캐시 처리 완료, 메시지 ID: {})", messageId);
                return; // 작업 종료
            }

            // L2 캐시 미스 시, L1 확인 및 OpenAI 호출
            DreamModel savedDream = handleCacheMiss(payload);

            // L2 캐시 쓰기
            writeToVectorCache(savedDream);

            stream.ack(groupName, messageId);
            log.info("꿈 해석 결과 DB 저장 및 ACK 성공 (메시지 ID: {})", messageId);

        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생 (메시지 ID: {}), ACK하지 않음, 나중에 재처리됨", messageId, e);
        }
    }

    /**
     * L2 벡터 캐시를 확인하고, 히트 시 꿈을 저장 후 true를 반환함
     * @return 캐시로 처리되었는지 여부
     */
    private boolean handleVectorCacheHit(DreamJobPayload payload) {
        log.info("[L2 캐시 확인] 사용자 ID: {}의 꿈 내용으로 유사 꿈 검색 시작함", payload.userId());
        EmbeddingSearchResponse searchResponse = embeddingService.searchSimilarDream(
                payload.dreamContent(), payload.style(), payload.dreamEmotion()
        ).block();

        if (searchResponse == null || !searchResponse.hasResult()) {
            return false; // 캐시 미스
        }

        log.info("[L2 캐시 히트] 유사 꿈 ID: {} 찾음, 기존 해석 재사용함", searchResponse.getDreamId());
        DreamModel cachedDream = dreamService.findDreamById(searchResponse.getDreamId());

        if (cachedDream == null) {
            log.warn("[L2 캐시 경고] 유사 꿈 ID: {} 찾았으나 DB에서 조회 실패함, 일반 흐름으로 진행함", searchResponse.getDreamId());
            return false; // DB 조회 실패 시 캐시 미스로 처리
        }

        DreamModel newDreamWithCache = createDreamModel(payload, cachedDream.getInterpretationResult(), cachedDream.getDreamTypes());
        dreamService.saveDream(newDreamWithCache);
        log.info("[L2 캐시 히트] 사용자 ID: {}의 꿈을 캐시된 해석으로 저장 완료함", payload.userId());
        return true; // 캐시 히트 및 처리 완료
    }

    /**
     * 캐시 미스 시, L1 심볼 캐시 확인 및 OpenAI 호출을 통해 꿈을 해석하고 저장함
     */
    private DreamModel handleCacheMiss(DreamJobPayload payload) {
        log.info("[L2 캐시 미스] 유사 꿈 없음, L1 심볼 캐시 확인 및 OpenAI 해석 진행함");

        // L1 심볼 캐시 확인은 dreamInterpretationGeneratorService 호출 시 자동으로 처리됨
        List<DreamType> extractedTypes = dreamSymbolExtractor.extractSymbols(payload.dreamContent());
        String interpretation = dreamInterpretationGeneratorService.generateInterpretation(
                payload.style(), payload.dreamEmotion(), extractedTypes, payload.dreamContent());

        DreamModel dreamModelToSave = createDreamModel(payload, interpretation, extractedTypes);
        return dreamService.saveDream(dreamModelToSave);
    }

    /**
     * 새로 저장된 꿈을 L2 벡터 캐시에 비동기적으로 추가함
     */
    private void writeToVectorCache(DreamModel savedDream) {
        log.info("[L2 캐시 쓰기] 새 꿈 ID: {}를 벡터 DB에 추가함", savedDream.getId());
        embeddingService.addDreamVector(
                        savedDream.getId(),
                        savedDream.getDreamContent(),
                        savedDream.getAiStyle(),
                        savedDream.getDreamEmotion()
                )
                .subscribeOn(Schedulers.boundedElastic()) // 비동기 실행을 위해 스케줄러 지정
                .subscribe(
                        responseMessage -> log.info("[L2 캐시 쓰기 성공] 응답: {}", responseMessage), // onNext
                        error -> log.error("[L2 캐시 쓰기 실패] 꿈 ID: {} 벡터 DB 추가 중 에러 발생", savedDream.getId(), error) // onError
                );
    }

    private DreamModel createDreamModel(DreamJobPayload payload, String interpretation, List<DreamType> extractedTypes) {
        SaveDreamRequest request = SaveDreamRequest.builder()
                .userId(payload.userId())
                .dreamContent(payload.dreamContent())
                .interpretationResult(interpretation)
                .dreamEmotion(payload.dreamEmotion())
                .aiStyle(payload.style())
                .dreamTypes(extractedTypes)
                .tags(payload.tags())
                .build();
        return DreamModel.createNewDream(request);
    }
}
