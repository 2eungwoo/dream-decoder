package sideprojects.dreamdecoder.application.web.dream.service.interpreter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.web.dream.service.DreamService;
import sideprojects.dreamdecoder.domain.dream.model.DreamModel;
import sideprojects.dreamdecoder.infrastructure.external.embedding.dto.EmbeddingSearchResponse;
import sideprojects.dreamdecoder.infrastructure.external.embedding.service.EmbeddingService;

@Slf4j
@Service
@RequiredArgsConstructor
public class VectorCacheDreamInterpreter implements DreamInterpreter {

    private final DreamInterpreter openAiDreamInterpreter;
    private final EmbeddingService embeddingService;
    private final DreamService dreamService;

    @Override
    public InterpretationResult interpret(DreamJobPayload payload) {
        log.info("[L2 캐시 확인] 사용자 ID: {}의 꿈 내용으로 유사 꿈 검색 시작함", payload.userId());
        EmbeddingSearchResponse searchResponse = embeddingService.searchSimilarDream(
                payload.dreamContent(), payload.style(), payload.dreamEmotion()
        );

        if (searchResponse != null && searchResponse.hasResult()) {
            log.info("[L2 캐시 히트] 유사 꿈 ID: {} 찾음, 기존 해석 재사용함", searchResponse.getDreamId());
            DreamModel cachedDream = dreamService.findDreamById(searchResponse.getDreamId());

            if (cachedDream != null) {
                // 캐시된 결과가 있으면 바로 InterpretationResult를 만들어 반환
                return new InterpretationResult(cachedDream.getInterpretationResult(), cachedDream.getDreamTypes());
            } else {
                log.warn("[L2 캐시 경고] 유사 꿈 ID: {} 찾았으나 DB에서 조회 실패함, 일반 흐름으로 진행함", searchResponse.getDreamId());
            }
        }

        log.info("[L2 캐시 미스] 유사 꿈 없음, L1 심볼 캐시 확인 및 OpenAI 해석 진행함");
        // 캐시가 없으면 위임받은 delegate(OpenAiDreamInterpreter)에게 작업을 넘김
        return openAiDreamInterpreter.interpret(payload);
    }
}
