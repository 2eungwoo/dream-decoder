package sideprojects.dreamdecoder.infrastructure.external.openai.util.exception;

import sideprojects.dreamdecoder.global.shared.exception.CustomException;

public class SemaphoreAcquireException extends CustomException {
    public SemaphoreAcquireException(OpenAiErrorCode errorCode) {
        super(errorCode);
    }
}
