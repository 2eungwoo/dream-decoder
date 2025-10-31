package sideprojects.dreamdecoder.presentation.feedback.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FeedbackRequest(

    @NotNull
    Boolean isSatisfied,

    @NotBlank
    String dreamQuery,

    String cachedQuery,

    Double similarityScore
) {
}
