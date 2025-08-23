package sideprojects.dreamdecoder.infrastructure.external.openai.util.exception;

import sideprojects.dreamdecoder.global.shared.exception.CustomException;

public class AiServerBusyException extends CustomException {
    public AiServerBusyException(OpenAiErrorCode errorCode, Throwable cause) {
        super(cause.getMessage(), errorCode);
    }
}
