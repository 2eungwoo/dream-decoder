package sideprojects.dreamdecoder.infrastructure.external.openai;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sideprojects.dreamdecoder.global.shared.response.ApiResponse;


import sideprojects.dreamdecoder.infrastructure.external.openai.dto.response.OpenAiResponseCode;

import sideprojects.dreamdecoder.infrastructure.external.openai.service.DreamInterpretationService;
import sideprojects.dreamdecoder.presentation.dream.dto.request.DreamInterpretationRequest;
import sideprojects.dreamdecoder.presentation.dream.dto.response.DreamInterpretationResponse;


@RestController
@RequiredArgsConstructor
public class AiChatController {
    private final DreamInterpretationService dreamInterpretationService;
    

    @PostMapping("/ai/chat")
    public ResponseEntity<ApiResponse<DreamInterpretationResponse>> interpret(
            @RequestParam Long userId,
            @Valid @RequestBody DreamInterpretationRequest request) {

        DreamInterpretationResponse result = dreamInterpretationService.interpretDream(userId, request.getDreamContent(), request.getStyle());
        return ApiResponse.success(OpenAiResponseCode.AI_CHAT_SUCCESS, result);
    }
}
