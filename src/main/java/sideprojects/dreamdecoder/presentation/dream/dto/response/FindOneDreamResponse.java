package sideprojects.dreamdecoder.presentation.dream.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import sideprojects.dreamdecoder.domain.dream.model.DreamModel;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

public record FindOneDreamResponse(
    Long id,
    Long userId,
    String dreamContent,
    String interpretationResult,
    AiStyle aiStyle,
    List<SymbolDto> symbols,
    LocalDateTime interpretedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static FindOneDreamResponse of(DreamModel dreamModel){
        List<SymbolDto> symbols = dreamModel.getDreamTypes().stream()
                .map(SymbolDto::of)
                .collect(Collectors.toList());

        return new FindOneDreamResponse(
            dreamModel.getId(),
            dreamModel.getUserId(),
            dreamModel.getDreamContent(),
            dreamModel.getInterpretationResult(),
            dreamModel.getAiStyle(),
            symbols,
            dreamModel.getInterpretedAt(),
            dreamModel.getCreatedAt(),
            dreamModel.getUpdatedAt()
        );
    }
}
