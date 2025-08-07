package sideprojects.dreamdecoder.infrastructure.external.openai.dto.response;

import java.util.List;

public record OpenAiChatResponse(List<OpenAiChatResponse.Choice> choices) {

    public record Choice(OpenAiChatResponse.Message message) {
    }

    public record Message(String content, String role) {
    }
}
