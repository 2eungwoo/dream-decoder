package sideprojects.dreamdecoder.presentation.openai.dto.response;

import sideprojects.dreamdecoder.presentation.dream.dto.response.DreamInterpretationResponse;

public record AiChatResponse(
        String interpretationId,
        DreamInterpretationResponse interpretationResult
) {
}
