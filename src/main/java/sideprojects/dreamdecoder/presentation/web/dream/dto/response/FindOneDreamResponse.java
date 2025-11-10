package sideprojects.dreamdecoder.presentation.web.dream.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import sideprojects.dreamdecoder.domain.dream.model.DreamModel;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

public record FindOneDreamResponse(
    Long id,
    Long userId,
    String dreamContent,
    String interpretationResult,
    DreamEmotion dreamEmotion,
    String tags,
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
            dreamModel.getDreamEmotion(),
            dreamModel.getTags(),
            dreamModel.getAiStyle(),
            symbols,
            dreamModel.getInterpretedAt(),
            dreamModel.getCreatedAt(),
            dreamModel.getUpdatedAt()
        );
    }
}
