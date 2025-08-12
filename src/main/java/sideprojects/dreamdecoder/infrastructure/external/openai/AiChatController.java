package sideprojects.dreamdecoder.infrastructure.external.openai;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sideprojects.dreamdecoder.global.shared.response.ApiResponse;
import sideprojects.dreamdecoder.infrastructure.external.openai.dto.request.ChooseAiStyleRequest;
import sideprojects.dreamdecoder.infrastructure.external.openai.dto.response.ChooseAiStyleResponse;
import sideprojects.dreamdecoder.infrastructure.external.openai.dto.response.OpenAiResponseCode;
import sideprojects.dreamdecoder.infrastructure.external.openai.service.AiStyleService;
import sideprojects.dreamdecoder.infrastructure.external.openai.service.DreamInterpretationService;
import sideprojects.dreamdecoder.presentation.dream.dto.request.DreamInterpretationRequest;
import sideprojects.dreamdecoder.presentation.dream.dto.response.DreamInterpretationResponse;


@RestController
@RequiredArgsConstructor
public class AiChatController {
    private final DreamInterpretationService dreamInterpretationService;
    private final AiStyleService aiStyleService;

    @PostMapping("/ai/chat")
    public ResponseEntity<ApiResponse<DreamInterpretationResponse>> interpret(
            @RequestParam Long userId,
            @Valid @RequestBody DreamInterpretationRequest request) {

        DreamInterpretationResponse result = dreamInterpretationService.interpretDream(userId, request.getDreamContent());
        return ApiResponse.success(OpenAiResponseCode.AI_CHAT_SUCCESS, result);
    }

    @PostMapping("/ai/style")
    public ResponseEntity<ApiResponse<ChooseAiStyleResponse>> setAiStyle(@RequestParam Long userId, @RequestBody ChooseAiStyleRequest request) {
        aiStyleService.chooseStyle(userId, request.getStyle());
        ChooseAiStyleResponse response = ChooseAiStyleResponse.of(request.getStyle());
        return ApiResponse.success(OpenAiResponseCode.AI_STYLE_SET_SUCCESS, response);
    }
}
