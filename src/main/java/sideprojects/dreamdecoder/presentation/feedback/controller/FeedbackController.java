package sideprojects.dreamdecoder.presentation.feedback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sideprojects.dreamdecoder.application.feedback.service.FeedbackService;
import sideprojects.dreamdecoder.global.shared.response.ApiResponse;
import sideprojects.dreamdecoder.global.shared.response.CommonResponseCode;
import sideprojects.dreamdecoder.presentation.feedback.dto.FeedbackRequest;
import sideprojects.dreamdecoder.presentation.feedback.dto.FeedbackResponseCode;

@RestController
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/feedback")
    public ResponseEntity<ApiResponse<Long>> submitFeedback(@RequestParam Long userId,
                                                            @Valid @RequestBody FeedbackRequest request) {

        Long feedbackId = feedbackService.saveFeedback(userId, request);

        return ApiResponse.success(FeedbackResponseCode.FEEDBACK_SUBMIT_SUCCESS, feedbackId);
    }
}
