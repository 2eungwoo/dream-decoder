package sideprojects.dreamdecoder.presentation.web.dream.dto.response;

import sideprojects.dreamdecoder.domain.dream.model.DreamModel;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

import java.util.List;
import java.util.stream.Collectors;

public record DreamInterpretationResponse(
        String interpretation,
        DreamEmotion dreamEmotion,
        String tags,
        AiStyle style,
        List<SymbolDto> symbols
) {

    public static DreamInterpretationResponse of(String interpretation, DreamEmotion dreamEmotion, String tags, AiStyle style, List<DreamType> dreamTypes) {
        List<SymbolDto> symbols = dreamTypes.stream()
                .map(SymbolDto::of)
                .collect(Collectors.toList());

        return new DreamInterpretationResponse(interpretation, dreamEmotion, tags, style, symbols);
    }

    public static DreamInterpretationResponse of(DreamModel dreamModel) {
        List<SymbolDto> symbols = dreamModel.getDreamTypes().stream()
                .map(SymbolDto::of)
                .collect(Collectors.toList());

        return new DreamInterpretationResponse(
                dreamModel.getInterpretationResult(),
                dreamModel.getDreamEmotion(),
                dreamModel.getTags(),
                dreamModel.getAiStyle(),
                symbols
        );
    }
}
