package sideprojects.dreamdecoder.infrastructure.external.openai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.config.OpenAiClient;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.PromptGenerator;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterpretationProvider {

    private final OpenAiClient openAiClient;

    @Cacheable(cacheNames = "dreamInterpretations", keyGenerator = "dreamInterpretationKeyGenerator")
    public String generateInterpretation(AiStyle style, DreamEmotion dreamEmotion, List<DreamType> extractedTypes, String dreamContent) {
        log.info("[L1 캐시 미스] OpenAI API 호출하여 꿈 해석 생성");
        String systemPrompt = PromptGenerator.generateInterpretationSystemPrompt(style, dreamEmotion, extractedTypes);
        return openAiClient.chat(systemPrompt, dreamContent);
    }
}
