package sideprojects.dreamdecoder.presentation.dream.dto.response;

import java.time.LocalDateTime;
import sideprojects.dreamdecoder.domain.dream.model.DreamModel;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

public record FindOneDreamResponse(
    Long id,
    Long userId,
    String dreamContent,
    String interpretationResult,
    AiStyle aiStyle,
    LocalDateTime interpretedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static FindOneDreamResponse of(DreamModel dreamModel){
        return new FindOneDreamResponse(
            dreamModel.getId(),
            dreamModel.getUserId(),
            dreamModel.getDreamContent(),
            dreamModel.getInterpretationResult(),
            dreamModel.getAiStyle(),
            dreamModel.getInterpretedAt(),
            dreamModel.getCreatedAt(),
            dreamModel.getUpdatedAt()
        );
    }
}
