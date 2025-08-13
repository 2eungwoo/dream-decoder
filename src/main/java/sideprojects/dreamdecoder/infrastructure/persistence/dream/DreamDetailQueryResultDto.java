package sideprojects.dreamdecoder.infrastructure.persistence.dream;

import com.querydsl.core.annotations.QueryProjection;
import sideprojects.dreamdecoder.domain.dream.persistence.DreamSymbol;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

import java.time.LocalDateTime;

public record DreamDetailQueryResultDto(
    Long id,
    Long userId,
    String dreamContent,
    String interpretationResult,
    AiStyle aiStyle,
    LocalDateTime interpretedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    DreamSymbol dreamSymbol
) {
    @QueryProjection
    public DreamDetailQueryResultDto {
    }
}
