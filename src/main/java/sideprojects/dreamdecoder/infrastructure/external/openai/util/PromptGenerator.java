package sideprojects.dreamdecoder.infrastructure.external.openai.util;

import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

public class PromptGenerator {

    public static String generateSystemPrompt(AiStyle style) {
        return style.getSystemPrompt();
    }
}