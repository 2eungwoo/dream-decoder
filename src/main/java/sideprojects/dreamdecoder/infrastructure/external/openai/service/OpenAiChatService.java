package sideprojects.dreamdecoder.infrastructure.external.openai.service;

import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.infrastructure.external.openai.config.OpenAiClient;
import sideprojects.dreamdecoder.infrastructure.external.openai.dto.response.AiChatResponse;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.PromptGenerator;

@Service
public class OpenAiChatService {

    private final AiStyleService aiStyleService;
    private final OpenAiClient openAiClient;

    public OpenAiChatService(AiStyleService aiStyleService, OpenAiClient openAiClient) {
        this.aiStyleService = aiStyleService;
        this.openAiClient = openAiClient;
    }

    public AiChatResponse chat(Long userId, String prompt) {
        AiStyle style = aiStyleService.getChosenStyle(userId);
        String systemPrompt = PromptGenerator.generateSystemPrompt(style);
        String aiResponseContent = openAiClient.chat(systemPrompt, prompt);
        return AiChatResponse.of(aiResponseContent, style);
    }
}