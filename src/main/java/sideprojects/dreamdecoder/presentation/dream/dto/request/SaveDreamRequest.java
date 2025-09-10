package sideprojects.dreamdecoder.presentation.dream.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

import java.util.List;

@Builder
public record SaveDreamRequest(
    @NotNull Long userId,
    @NotBlank String dreamContent,
    @NotBlank String interpretationResult,
    @NotNull DreamEmotion dreamEmotion,
    @Size(max = 255) String tags,
    @NotNull AiStyle aiStyle,
    List<DreamType> dreamTypes
) {
}
