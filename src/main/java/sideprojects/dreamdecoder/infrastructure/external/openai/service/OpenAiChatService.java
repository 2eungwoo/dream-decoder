package sideprojects.dreamdecoder.infrastructure.external.openai.service;

import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.infrastructure.external.openai.config.OpenAiClient;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.PromptGenerator;

@Service
public class OpenAiChatService {

    private final AiStyleService aiStyleService;
    private final OpenAiClient openAiClient;

    public OpenAiChatService(AiStyleService aiStyleService, OpenAiClient openAiClient) {
        this.aiStyleService = aiStyleService;
        this.openAiClient = openAiClient;
    }

    public String chat(Long userId, String prompt) {
        String style = aiStyleService.getStyle(userId);
        String systemPrompt = PromptGenerator.generateSystemPrompt(style);
        return openAiClient.chat(systemPrompt, prompt);
    }
}