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
import sideprojects.dreamdecoder.infrastructure.external.embedding.dto.EmbeddingResponse;
import sideprojects.dreamdecoder.infrastructure.external.embedding.service.EmbeddingService;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.infrastructure.external.openai.service.DreamInterpretationGeneratorService;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.DreamSymbolExtractor;
import sideprojects.dreamdecoder.presentation.dream.dto.request.SaveDreamRequest;

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

            // 벡터 생성 로직 추가
            log.info("[Vector-DEBUG] 임베딩 생성 시작, payload.dreamContent: {}", payload.dreamContent());
            EmbeddingResponse embeddingResponse = embeddingService.createEmbedding(payload.dreamContent()).block();

            if (embeddingResponse != null && embeddingResponse.getVector() != null) {
                log.info("[Vector-DEBUG] 생성된 벡터(0~5까지만): {}", embeddingResponse.getVector().subList(0, 5));
            } else {
                log.warn("[Vector-DEBUG] 임베딩 생성 실패");
            }

            List<DreamType> extractedTypes = dreamSymbolExtractor.extractSymbols(payload.dreamContent());
            String interpretation = dreamInterpretationGeneratorService.generateInterpretation(
                    payload.style(), payload.dreamEmotion(), extractedTypes, payload.dreamContent());

            DreamModel dreamModelToSave = createDreamModel(payload, interpretation, extractedTypes);
            dreamService.saveDream(dreamModelToSave);

            // 처리 성공 시 ACK
            stream.ack(groupName, messageId);
            log.info("꿈 해석 결과 DB 저장 및 ACK 성공 (메시지 ID: {})", messageId);
        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생 (메시지 ID: {}), ACK하지 않음. 나중에 재처리됩니다.", messageId, e);
            // ACK를 보내지 않으면 메시지는 PEL에 남아있게 됨
        }
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
