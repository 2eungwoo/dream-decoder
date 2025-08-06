package sideprojects.dreamdecoder.global.shared.exception;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import sideprojects.dreamdecoder.global.shared.response.ErrorCode;
import sideprojects.dreamdecoder.global.shared.response.ErrorResponse;
import sideprojects.dreamdecoder.global.shared.response.ErrorResponse.FieldError;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private List<FieldError> errors = new ArrayList<>();

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, List<ErrorResponse.FieldError> errors) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.errors = errors;
    }
}