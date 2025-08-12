package sideprojects.dreamdecoder.presentation.dream.dto.response;

import java.time.LocalDateTime;
import sideprojects.dreamdecoder.domain.dream.model.Dream;
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

    public static FindOneDreamResponse of(Dream dream){
        return new FindOneDreamResponse(
            dream.getId(),
            dream.getUserId(),
            dream.getDreamContent(),
            dream.getInterpretationResult(),
            dream.getAiStyle(),
            dream.getInterpretedAt(),
            dream.getCreatedAt(),
            dream.getUpdatedAt()
        );
    }
}
