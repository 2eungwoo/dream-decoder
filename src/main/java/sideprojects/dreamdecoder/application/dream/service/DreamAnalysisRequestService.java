package sideprojects.dreamdecoder.application.dream.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.dream.producer.DreamSaveJobCommand;
import sideprojects.dreamdecoder.application.dream.producer.DreamSaveJobProducer;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.presentation.dream.dto.request.DreamInterpretationRequest;

@Service
@RequiredArgsConstructor
public class DreamAnalysisRequestService {

    private final DreamSaveJobProducer dreamSaveJobProducer;

    // OpenAI 호출 없이, 해몽에 필요한 정보만 담아 Redis에 즉시 발행
    public void requestAnalysis(Long userId, String idempotencyKey, DreamInterpretationRequest request) {
        DreamSaveJobCommand command = DreamSaveJobCommand.builder()
                .userId(userId)
                .dreamContent(request.getDreamContent())
                .dreamEmotion(request.getDreamEmotion())
                .tags(request.getTags())
                .style(AiStyle.from(request.getStyle()))
                // interpretation, types 필드는 백그라운드에서 채울거라 여기서는 제외
                .build();

        dreamSaveJobProducer.publishJob(command);
    }
}
