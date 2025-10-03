package sideprojects.dreamdecoder.presentation.openai;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sideprojects.dreamdecoder.application.dream.service.DreamAnalysisRequestService;
import sideprojects.dreamdecoder.global.aop.PreventDuplicateRequest;
import sideprojects.dreamdecoder.global.shared.response.ApiResponse;
import sideprojects.dreamdecoder.infrastructure.external.openai.dto.response.OpenAiResponseCode;
import sideprojects.dreamdecoder.presentation.dream.dto.request.DreamInterpretationRequest;
import sideprojects.dreamdecoder.presentation.openai.limiter.aop.UsageLimitCheck;


@RestController
@RequiredArgsConstructor
public class AiChatController {

    private final DreamAnalysisRequestService dreamAnalysisRequestService;

    @PostMapping("/ai/chat")
    @UsageLimitCheck
    @PreventDuplicateRequest(key = "#userId + ':' + #request.hashCode()")
    public ResponseEntity<ApiResponse<String>> interpret(
            @RequestParam Long userId,
            @Valid @RequestBody DreamInterpretationRequest request) {

        dreamAnalysisRequestService.requestAnalysis(userId, request);

        return ApiResponse.success(OpenAiResponseCode.AI_CHAT_REQUEST_SUCCESS, "해몽 분석 요청이 성공적으로 접수되었습니다. 잠시 후 확인해주세요.");
    }
}
