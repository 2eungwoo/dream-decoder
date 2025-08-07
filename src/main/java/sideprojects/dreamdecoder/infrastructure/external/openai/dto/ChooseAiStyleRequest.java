package sideprojects.dreamdecoder.infrastructure.external.openai.dto;

import lombok.Getter;
import lombok.Setter;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

@Getter
@Setter
public class ChooseAiStyleRequest {
    private AiStyle style;
}