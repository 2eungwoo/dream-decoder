package sideprojects.dreamdecoder.global.shared.exception;

public class ForbiddenAccessException extends CustomException {
    public ForbiddenAccessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
