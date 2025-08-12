package sideprojects.dreamdecoder.presentation.dream.dto.response;

import java.time.LocalDateTime;
import sideprojects.dreamdecoder.domain.dream.model.DreamModel;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

public record FindAllDreamResponse(
    Long id,
    Long userId,
    AiStyle aiStyle,
    LocalDateTime interpretedAt,
    LocalDateTime createdAt
) {
    public static FindAllDreamResponse of(DreamModel dreamModel){
        return new FindAllDreamResponse(
            dreamModel.getId(),
            dreamModel.getUserId(),
            dreamModel.getAiStyle(),
            dreamModel.getInterpretedAt(),
            dreamModel.getCreatedAt()
        );
    }
}
