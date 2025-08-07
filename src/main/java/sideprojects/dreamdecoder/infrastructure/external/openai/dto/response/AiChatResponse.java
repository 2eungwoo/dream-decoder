package sideprojects.dreamdecoder.infrastructure.external.openai.dto.response;

import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

public record AiChatResponse(String aiResponseContent, AiStyle usedStyle) {

    public static AiChatResponse of(String aiResponseContent, AiStyle usedStyle) {
        return new AiChatResponse(aiResponseContent, usedStyle);
    }
}
