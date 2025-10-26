package sideprojects.dreamdecoder.application.dream.service.interpreter;

import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

import java.util.Map;

public record DreamJobPayload(Long userId, String dreamContent, DreamEmotion dreamEmotion, AiStyle style, String tags) {
    public static DreamJobPayload fromMessage(Map<String, String> messageBody) {
        return new DreamJobPayload(
                Long.parseLong(messageBody.get("userId")),
                messageBody.get("dreamContent"),
                DreamEmotion.valueOf(messageBody.get("dreamEmotion")),
                AiStyle.valueOf(messageBody.get("style")),
                messageBody.get("tags")
        );
    }
}
