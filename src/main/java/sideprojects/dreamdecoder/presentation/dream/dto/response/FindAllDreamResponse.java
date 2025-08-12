package sideprojects.dreamdecoder.presentation.dream.dto.response;

import java.time.LocalDateTime;
import sideprojects.dreamdecoder.domain.dream.model.Dream;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

public record FindAllDreamResponse(
    Long id,
    Long userId,
    AiStyle aiStyle,
    LocalDateTime interpretedAt,
    LocalDateTime createdAt
) {
    public static FindAllDreamResponse of(Dream dream){
        return new FindAllDreamResponse(
            dream.getId(),
            dream.getUserId(),
            dream.getAiStyle(),
            dream.getInterpretedAt(),
            dream.getCreatedAt()
        );
    }
}
