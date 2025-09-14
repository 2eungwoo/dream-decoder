package sideprojects.dreamdecoder.infrastructure.external.openai.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheDataValidator {

    private final InterpretationCacheManager cacheManager;

    public String getOrGenerate(Long userId, String dreamContent, DreamEmotion dreamEmotion, String tags, AiStyle style, Supplier<String> generator) {
        Optional<String> cachedInterpretation = cacheManager.get(userId, dreamContent, dreamEmotion, tags, style);

        if (cachedInterpretation.isPresent()) {
            log.info("캐시에서 AI 해석 결과를 찾았습니다. (유저 ID: {})", userId);
            return cachedInterpretation.get();
        }

        log.info("AI 서비스 요청 처리 시작 (유저 ID: {})", userId);
        String newInterpretation = generator.get();
        cacheManager.set(userId, dreamContent, dreamEmotion, tags, style, newInterpretation);
        log.info("AI 해석 결과를 캐시에 저장했습니다. (유저 ID: {})", userId);

        return newInterpretation;
    }
}
