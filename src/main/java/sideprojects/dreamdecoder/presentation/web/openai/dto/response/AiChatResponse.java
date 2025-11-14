package sideprojects.dreamdecoder.presentation.web.openai.dto.response;

import sideprojects.dreamdecoder.presentation.web.dream.dto.response.DreamInterpretationResponse;

public record AiChatResponse(
        String interpretationId,
        DreamInterpretationResponse interpretationResult
) {
}
