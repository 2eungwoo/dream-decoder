package sideprojects.dreamdecoder.application.web.dream.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.web.dream.service.interpreter.DreamInterpreter;
import sideprojects.dreamdecoder.application.web.dream.service.interpreter.DreamJobPayload;
import sideprojects.dreamdecoder.application.web.dream.service.interpreter.InterpretationResult;
import sideprojects.dreamdecoder.domain.dream.model.DreamModel;
import sideprojects.dreamdecoder.infrastructure.external.embedding.service.EmbeddingService;
import sideprojects.dreamdecoder.presentation.web.dream.dto.request.SaveDreamRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class DreamProcessingService {

    private final DreamInterpreter vectorCacheDreamInterpreter;
    private final DreamService dreamService;
    private final EmbeddingService embeddingService;

    public void interpretAndSave(DreamJobPayload payload) {
        // 1. 꿈 해석 (캐시 확인 -> OpenAI 호출)
        InterpretationResult result = vectorCacheDreamInterpreter.interpret(payload);

        // 2. 결과 기반으로 DreamModel 생성
        DreamModel dreamModelToSave = createDreamModel(payload, result);

        // 3. DB에 저장
        DreamModel savedDream = dreamService.saveDream(dreamModelToSave);
        log.info("꿈 해석 결과 DB 저장 완료 (새 꿈 ID: {})", savedDream.getId());

        // 4. L2 벡터 캐시에 비동기적으로 추가
        writeToVectorCache(savedDream);
    }

    private DreamModel createDreamModel(DreamJobPayload payload, InterpretationResult result) {
        SaveDreamRequest request = SaveDreamRequest.builder()
            .userId(payload.userId())
            .dreamContent(payload.dreamContent())
            .interpretationResult(result.interpretation())
            .dreamEmotion(payload.dreamEmotion())
            .aiStyle(payload.style())
            .dreamTypes(result.dreamTypes())
            .tags(payload.tags())
            .build();
        return DreamModel.createNewDream(request);
    }

    private void writeToVectorCache(DreamModel savedDream) {
        log.info("[L2 캐시 쓰기] 새 꿈 ID: {}를 벡터 DB에 추가함", savedDream.getId());
        embeddingService.addDreamVector(
            savedDream.getId(),
            savedDream.getDreamContent(),
            savedDream.getAiStyle(),
            savedDream.getDreamEmotion()
        );
    }
}
