package sideprojects.dreamdecoder.infrastructure.external.openai;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sideprojects.dreamdecoder.global.shared.response.ApiResponse;
import sideprojects.dreamdecoder.infrastructure.external.openai.dto.response.OpenAiResponseCode;
import sideprojects.dreamdecoder.infrastructure.external.openai.dto.response.AiChatResponse;
import sideprojects.dreamdecoder.infrastructure.external.openai.dto.request.ChooseAiStyleRequest;
import sideprojects.dreamdecoder.infrastructure.external.openai.dto.response.ChooseAiStyleResponse;
import sideprojects.dreamdecoder.infrastructure.external.openai.service.AiStyleService;
import sideprojects.dreamdecoder.infrastructure.external.openai.service.OpenAiChatService;


@RestController
@RequiredArgsConstructor
public class AiChatController {
    private final OpenAiChatService openAiChatService;
    private final AiStyleService aiStyleService;

    @PostMapping("/ai/chat")
    public ResponseEntity<ApiResponse<AiChatResponse>> chat(@RequestParam Long userId, @RequestBody String prompt) {
        AiChatResponse result = openAiChatService.chat(userId, prompt);
        return ApiResponse.success(OpenAiResponseCode.AI_CHAT_SUCCESS, result);
    }

    @PostMapping("/ai/style")
    public ResponseEntity<ApiResponse<ChooseAiStyleResponse>> setAiStyle(@RequestParam Long userId, @RequestBody ChooseAiStyleRequest request) {
        aiStyleService.chooseStyle(userId, request.getStyle());
        ChooseAiStyleResponse response = ChooseAiStyleResponse.of(request.getStyle());
        return ApiResponse.success(OpenAiResponseCode.AI_STYLE_SET_SUCCESS, response);
    }
}
