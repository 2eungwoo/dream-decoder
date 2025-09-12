package sideprojects.dreamdecoder.presentation.openai;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sideprojects.dreamdecoder.application.dream.util.InterpretationCacheService;
import sideprojects.dreamdecoder.global.aop.PreventDuplicateRequest;
import sideprojects.dreamdecoder.global.shared.response.ApiResponse;
import sideprojects.dreamdecoder.infrastructure.external.openai.dto.response.OpenAiResponseCode;
import sideprojects.dreamdecoder.infrastructure.external.openai.service.DreamInterpretationService;
import sideprojects.dreamdecoder.infrastructure.external.openai.service.DummyService;
import sideprojects.dreamdecoder.presentation.dream.dto.request.DreamInterpretationRequest;
import sideprojects.dreamdecoder.presentation.dream.dto.response.DreamInterpretationResponse;
import sideprojects.dreamdecoder.presentation.openai.dto.response.AiChatResponse;


@RestController
@RequiredArgsConstructor
public class AiChatController {

    private final DreamInterpretationService dreamInterpretationService;
    private final DummyService dummyDreamInterpretationService;
    private final InterpretationCacheService interpretationCacheService;

    @PostMapping("/ai/chat")
    @PreventDuplicateRequest(key = "#userId + ':' + #request.hashCode()")
    public ResponseEntity<ApiResponse<AiChatResponse>> interpret(
        @RequestParam Long userId,
        @Valid @RequestBody DreamInterpretationRequest request) {

        DreamInterpretationResponse interpretationResult = dreamInterpretationService.interpretDream(
            userId,
            request.getDreamContent(),
            request.getDreamEmotion(),
            request.getTags(),
            request.getStyle()
        );

        String interpretationId = interpretationCacheService.cacheInterpretation(request,
            interpretationResult);

        AiChatResponse response = new AiChatResponse(interpretationId, interpretationResult);
        return ApiResponse.success(OpenAiResponseCode.AI_CHAT_SUCCESS, response);
    }

    /*
     * 성능 비교 테스트용으로 만든 더미 메소드
     */
    @PostMapping("/dummy/ai/chat")
    public ResponseEntity<ApiResponse<String>> interpretDreamDummy(
        @RequestParam Long userId,
        @Valid @RequestBody DreamInterpretationRequest request) throws InterruptedException {

        String dummyResult = dummyDreamInterpretationService.interpretDream(
            userId, request.getDreamContent());
        return ApiResponse.success(OpenAiResponseCode.AI_CHAT_SUCCESS, dummyResult);
    }
}
