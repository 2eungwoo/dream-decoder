package sideprojects.dreamdecoder.presentation.dream.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

import java.util.List;

@Builder
public record SaveDreamRequest(
    @NotNull Long userId,
    @NotBlank String dreamContent,
    @NotBlank String interpretationResult,
    @NotNull AiStyle aiStyle,
    List<DreamType> dreamTypes
) {
}
