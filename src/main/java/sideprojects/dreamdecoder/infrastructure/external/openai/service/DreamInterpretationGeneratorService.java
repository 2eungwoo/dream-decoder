package sideprojects.dreamdecoder.infrastructure.external.openai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.config.OpenAiClient;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.PromptGenerator;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class DreamInterpretationGeneratorService {

    private final OpenAiClient openAiClient;
    private final CacheManager cacheManager;
    private final KeyGenerator keyGenerator;

    public DreamInterpretationGeneratorService(OpenAiClient openAiClient, CacheManager cacheManager, @Qualifier("dreamInterpretationKeyGenerator") KeyGenerator keyGenerator) {
        this.openAiClient = openAiClient;
        this.cacheManager = cacheManager;
        this.keyGenerator = keyGenerator;
    }

    public String generateInterpretation(AiStyle style, DreamEmotion dreamEmotion, List<DreamType> extractedTypes, String dreamContent) {
        Cache cache = Objects.requireNonNull(cacheManager.getCache("dreamInterpretations"));
        Object key = generateCacheKey(style, dreamEmotion, extractedTypes, dreamContent);
        Cache.ValueWrapper valueWrapper = cache.get(key);

        if (valueWrapper != null) {
            log.info("[L1 캐시 히트] key: {}", key);
            return (String) valueWrapper.get();
        }

        log.info("[L1 캐시 미스] key: {}", key);
        String systemPrompt = PromptGenerator.generateInterpretationSystemPrompt(style, dreamEmotion, extractedTypes);
        String interpretation = openAiClient.chat(systemPrompt, dreamContent);
        cache.put(key, interpretation);

        return interpretation;
    }

    private Object generateCacheKey(AiStyle style, DreamEmotion dreamEmotion, List<DreamType> extractedTypes, String dreamContent) {
        try {
            Method method = getClass().getMethod("generateInterpretation", AiStyle.class, DreamEmotion.class, List.class, String.class);
            return keyGenerator.generate(this, method, style, dreamEmotion, extractedTypes, dreamContent);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("generateInterpretation 메소드를 찾을 수 없음", e);
        }
    }
}
