package sideprojects.dreamdecoder.presentation.web.feedback.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import sideprojects.dreamdecoder.global.shared.response.ResponseCode;

@Getter
@RequiredArgsConstructor
public enum FeedbackResponseCode implements ResponseCode {

    FEEDBACK_SUBMIT_SUCCESS(HttpStatus.OK, "FB001", "피드백이 성공적으로 제출되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
