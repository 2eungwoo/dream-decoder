package sideprojects.dreamdecoder.presentation.dream.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import sideprojects.dreamdecoder.domain.dream.model.DreamModel;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

public record SaveDreamResponse(
        Long id,
        Long userId,
        String interpretation,
        AiStyle style,
        List<SymbolDto> symbols
) {
    public static SaveDreamResponse of(DreamModel dreamModel) {
        List<SymbolDto> symbols = dreamModel.getDreamTypes().stream()
                .map(SymbolDto::of)
                .collect(Collectors.toList());

        return new SaveDreamResponse(
                dreamModel.getId(),
                dreamModel.getUserId(),
                dreamModel.getInterpretationResult(),
                dreamModel.getAiStyle(),
                symbols
        );
    }
}



