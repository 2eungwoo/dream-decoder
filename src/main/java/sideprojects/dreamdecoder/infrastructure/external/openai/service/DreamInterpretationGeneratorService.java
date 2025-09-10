package sideprojects.dreamdecoder.infrastructure.external.openai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.config.OpenAiClient;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.PromptGenerator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DreamInterpretationGeneratorService {

    private final OpenAiClient openAiClient;

    public String generateInterpretation(AiStyle style, DreamEmotion dreamEmotion, List<DreamType> extractedTypes, String dreamContent) {
        String systemPrompt = PromptGenerator.generateInterpretationSystemPrompt(style, dreamEmotion, extractedTypes);
        return openAiClient.chat(systemPrompt, dreamContent);
    }
}
