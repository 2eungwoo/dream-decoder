package sideprojects.dreamdecoder.global.shared.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C001", "Internal server error"),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C002", "Invalid input type"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C003", "Method not allowed"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C004", "Invalid type value"),
    BAD_CREDENTIALS(HttpStatus.BAD_REQUEST, "C005", "Bad credentials"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "C006", "Resource not found"),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "C007", "Missing request parameter"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "C008", "Bad Request");

    private final HttpStatus status;
    private final String code;
    private final String message;
}