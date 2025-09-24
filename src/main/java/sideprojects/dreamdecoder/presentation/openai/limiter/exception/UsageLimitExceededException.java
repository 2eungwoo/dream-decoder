package sideprojects.dreamdecoder.presentation.openai.limiter.exception;

import sideprojects.dreamdecoder.global.shared.exception.CustomException;
import sideprojects.dreamdecoder.global.shared.response.ErrorCode;

public class UsageLimitExceededException extends CustomException {

    public UsageLimitExceededException(ErrorCode errorCode) {
        super(errorCode);
    }
}
