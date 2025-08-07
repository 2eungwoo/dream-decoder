package sideprojects.dreamdecoder.presentation.dream.dto.response;

import sideprojects.dreamdecoder.domain.dream.model.Dream;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

import java.time.LocalDateTime;

public record DreamResponse(
    Long id,
    Long userId,
    String dreamContent,
    String interpretationResult,
    AiStyle aiStyle,
    LocalDateTime interpretedAt
) {
    public static DreamResponse of(Dream dream) {
        return new DreamResponse(
            dream.getId(),
            dream.getUserId(),
            dream.getDreamContent(),
            dream.getInterpretationResult(),
            dream.getAiStyle(),
            dream.getInterpretedAt()
        );
    }
}
