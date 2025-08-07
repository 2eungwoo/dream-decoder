package sideprojects.dreamdecoder.presentation.dream.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

public record SaveDreamRequest(
    @NotNull Long userId,
    @NotBlank String dreamContent,
    @NotBlank String interpretationResult,
    @NotNull AiStyle aiStyle
) {
}
