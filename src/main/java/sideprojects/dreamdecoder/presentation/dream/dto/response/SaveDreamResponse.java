package sideprojects.dreamdecoder.presentation.dream.dto.response;

import sideprojects.dreamdecoder.domain.dream.model.DreamModel;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

import java.time.LocalDateTime;

public record SaveDreamResponse(
    Long id,
    Long userId,
    String dreamContent,
    String interpretationResult,
    AiStyle aiStyle,
    LocalDateTime interpretedAt
) {
    public static SaveDreamResponse of(DreamModel dreamModel) {
        return new SaveDreamResponse(
            dreamModel.getId(),
            dreamModel.getUserId(),
            dreamModel.getDreamContent(),
            dreamModel.getInterpretationResult(),
            dreamModel.getAiStyle(),
            dreamModel.getInterpretedAt()
        );
    }
}
