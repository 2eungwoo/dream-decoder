package sideprojects.dreamdecoder.infrastructure.external.openai.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.dream.producer.DreamSaveJobProducer;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.global.aop.PreventDuplicateRequest;
import sideprojects.dreamdecoder.application.dream.producer.DreamSaveJobCommand;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.SemaphoreManager;
import sideprojects.dreamdecoder.presentation.dream.dto.response.DreamInterpretationResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class DreamInterpretationService {

    private final DreamSymbolExtractorService dreamSymbolExtractorService;
    private final DreamInterpretationGeneratorService dreamInterpretationGeneratorService;
    private final SemaphoreManager semaphoreManager;
    private final DreamSaveJobProducer dreamSaveJobProducer;

    @PreventDuplicateRequest(key = "#userId")
    public DreamInterpretationResponse interpretDream(Long userId, String dreamContent,
        DreamEmotion dreamEmotion, String tags, AiStyle style) {
        try {
            semaphoreManager.acquireSemaphore();
            return processDreamLogicAndPublishJob(userId, dreamContent, dreamEmotion, tags, style);
        } finally {
            semaphoreManager.releaseSemaphore();
        }
    }

    private DreamInterpretationResponse processDreamLogicAndPublishJob(Long userId,
        String dreamContent, DreamEmotion dreamEmotion, String tags, AiStyle style) {
        log.info("AI 서비스 요청 처리 시작 (유저 ID: {})", userId);

        AiStyle actualStyle = AiStyle.from(style);
        List<DreamType> extractedTypes = dreamSymbolExtractorService.extractSymbols(dreamContent);
        String interpretation = dreamInterpretationGeneratorService.generateInterpretation(
            actualStyle, dreamEmotion, extractedTypes, dreamContent);

        DreamSaveJobCommand command = DreamSaveJobCommand.builder()
                .userId(userId)
                .dreamContent(dreamContent)
                .interpretation(interpretation)
                .dreamEmotion(dreamEmotion)
                .tags(tags)
                .style(actualStyle)
                .types(extractedTypes)
                .build();
        dreamSaveJobProducer.publishJob(command);

        return DreamInterpretationResponse.of(interpretation, dreamEmotion, tags, actualStyle,
            extractedTypes);
    }
}