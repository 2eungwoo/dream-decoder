package sideprojects.dreamdecoder.domain.auth.util.exception;

import lombok.Getter;
import sideprojects.dreamdecoder.global.shared.exception.CustomException;
import sideprojects.dreamdecoder.global.shared.response.ErrorCode;

@Getter
public class AuthException extends CustomException {

    private final String message;
    private final ErrorCode errorCode;

    public AuthException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
        this.message = errorCode.getMessage();
        this.errorCode = errorCode;
    }
}
