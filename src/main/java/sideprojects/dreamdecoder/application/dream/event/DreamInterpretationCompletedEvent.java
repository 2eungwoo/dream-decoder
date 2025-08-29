package sideprojects.dreamdecoder.application.dream.event;

import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import java.util.List;

public record DreamInterpretationCompletedEvent(
        Long userId,
        String dreamContent,
        String interpretationResult,
        AiStyle aiStyle,
        List<DreamType> dreamTypes
) {
}
