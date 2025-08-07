package sideprojects.dreamdecoder.infrastructure.persistence.dream;

import com.querydsl.core.annotations.QueryProjection;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

import java.time.LocalDateTime;

public record DreamProjectionDto(
        Long id,
        Long userId,
        String dreamContent,
        String interpretationResult,
        AiStyle aiStyle,
        LocalDateTime interpretedAt
) {
    @QueryProjection
    public DreamProjectionDto {
    }
}
