package sideprojects.dreamdecoder.infrastructure.external.openai.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.dream.producer.DreamSaveJobCommand;
import sideprojects.dreamdecoder.application.dream.producer.DreamSaveJobProducer;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.global.aop.UseSemaphore;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.InterpretationCacheManager;
import sideprojects.dreamdecoder.presentation.dream.dto.response.DreamInterpretationResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class DreamInterpretationService {

    private final DreamSymbolExtractorService dreamSymbolExtractorService;
    private final DreamInterpretationGeneratorService dreamInterpretationGeneratorService;
    private final DreamSaveJobProducer dreamSaveJobProducer;
    private final InterpretationCacheManager cacheManager;

    @UseSemaphore
    public DreamInterpretationResponse interpretDream(Long userId, String dreamContent,
        DreamEmotion dreamEmotion, String tags, AiStyle style) {

        // 캐시 확인
        Optional<String> cachedInterpretation = cacheManager.get(userId, dreamContent);

        String interpretation;
        List<DreamType> extractedTypes = dreamSymbolExtractorService.extractSymbols(dreamContent);
        AiStyle actualStyle = AiStyle.from(style);

        if (cachedInterpretation.isPresent()) {
            // Cache Hit
            log.info("캐시에서 AI 해석 결과를 찾았습니다. (유저 ID: {})", userId);
            interpretation = cachedInterpretation.get();
        } else {
            // Cache Miss
            log.info("AI 서비스 요청 처리 시작 (유저 ID: {})", userId);
            interpretation = dreamInterpretationGeneratorService.generateInterpretation(
                actualStyle, dreamEmotion, extractedTypes, dreamContent);

            // 캐시 저장
            cacheManager.set(userId, dreamContent, interpretation);
            log.info("AI 해석 결과를 캐시에 저장했습니다. (유저 ID: {})", userId);
        }

        // DB 저장 작업 -> Redis Stream에 발행
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
