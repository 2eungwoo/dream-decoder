package sideprojects.dreamdecoder.presentation.web.openai.limiter.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import sideprojects.dreamdecoder.global.shared.response.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum UsageLimitErrorCode implements ErrorCode {

    USAGE_LIMIT_EXCEEDED(HttpStatus.FORBIDDEN, "UL001", "하루 사용 횟수(3회)를 초과했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
