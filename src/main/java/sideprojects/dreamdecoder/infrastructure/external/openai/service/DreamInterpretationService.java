package sideprojects.dreamdecoder.infrastructure.external.openai.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.dream.producer.DreamSaveJobCommand;
import sideprojects.dreamdecoder.application.dream.producer.DreamSaveJobProducer;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.global.aop.UseSemaphore;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.DreamSymbolExtractor;
import sideprojects.dreamdecoder.presentation.dream.dto.response.DreamInterpretationResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class DreamInterpretationService {

    private final DreamSymbolExtractor dreamSymbolExtractor;
    private final DreamInterpretationGeneratorService dreamInterpretationGeneratorService;
    private final DreamSaveJobProducer dreamSaveJobProducer;

    @UseSemaphore
    public DreamInterpretationResponse interpretDream(Long userId, String dreamContent,
        DreamEmotion dreamEmotion, String tags, AiStyle style) {

        AiStyle actualStyle = AiStyle.from(style);
        List<DreamType> extractedTypes = dreamSymbolExtractor.extractSymbols(dreamContent);

        // OpenAI 호출
        String interpretation = dreamInterpretationGeneratorService.generateInterpretation(
            actualStyle, dreamEmotion, extractedTypes, dreamContent);

        // DB 저장 ->  Redis Stream에 발행
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
