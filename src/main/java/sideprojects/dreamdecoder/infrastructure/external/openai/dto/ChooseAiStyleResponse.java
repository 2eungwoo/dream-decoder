package sideprojects.dreamdecoder.infrastructure.external.openai.dto;

import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

public record ChooseAiStyleResponse(AiStyle chosenStyle) {

    public static ChooseAiStyleResponse of(AiStyle chosenStyle) {
        return new ChooseAiStyleResponse(chosenStyle);
    }
}