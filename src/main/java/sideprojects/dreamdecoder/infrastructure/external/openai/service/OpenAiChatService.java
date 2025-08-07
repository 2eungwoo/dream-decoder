package sideprojects.dreamdecoder.infrastructure.external.openai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.dream.usecase.SaveDreamUseCase;
import sideprojects.dreamdecoder.domain.dream.model.Dream;
import sideprojects.dreamdecoder.infrastructure.external.openai.config.OpenAiClient;
import sideprojects.dreamdecoder.infrastructure.external.openai.dto.response.AiChatResponse;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.PromptGenerator;

@Service
@RequiredArgsConstructor
public class OpenAiChatService {

    private final AiStyleService aiStyleService;
    private final OpenAiClient openAiClient;
    private final SaveDreamUseCase saveDreamUseCase;

    public AiChatResponse chat(Long userId, String prompt) {
        AiStyle style = aiStyleService.getChosenStyle(userId);
        String systemPrompt = PromptGenerator.generateSystemPrompt(style);
        String aiResponseContent = openAiClient.chat(systemPrompt, prompt);

        Dream dreamToSave = Dream.createNewDream(userId, prompt, aiResponseContent, style);
        saveDreamUseCase.save(dreamToSave);

        return AiChatResponse.of(aiResponseContent, style);
    }
}