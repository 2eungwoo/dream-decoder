package sideprojects.dreamdecoder.infrastructure.external.openai.util.exception;

import sideprojects.dreamdecoder.global.shared.exception.CustomException;

public class OpenAiApiException extends CustomException {
    public OpenAiApiException(OpenAiErrorCode errorCode) {
        super(errorCode);
    }

    public OpenAiApiException(OpenAiErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), errorCode);
    }
}
