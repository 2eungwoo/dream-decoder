package sideprojects.dreamdecoder.global.shared.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String message;
    private HttpStatus status;
    private int statusCode;
    private String code;

    @JsonIgnore
    private ErrorCode errorCode;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<FieldError> errors;

    public ErrorResponse(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
        this.statusCode = errorCode.getStatus().value();
        this.code = errorCode.getCode();
        this.errors = Collections.emptyList();
    }

    public ErrorResponse(ErrorCode errorCode, List<FieldError> errors) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
        this.statusCode = errorCode.getStatus().value();
        this.code = errorCode.getCode();
        this.errors = errors;
    }

    // @Valid + @RequestBody에서 나오는 에러
    public static ErrorResponse of(final ErrorCode errorCode, final BindingResult bindingResult) {
        return new ErrorResponse(errorCode, FieldError.of(bindingResult));
    }

    // @Validated + @RequestParam, @PathVariable 에서 나오는 에러
    public static ErrorResponse of(final ErrorCode errorCode,
        final Set<ConstraintViolation<?>> violations) {
        return new ErrorResponse(errorCode, FieldError.of(violations));
    }

    // No-Parameter
    public static ErrorResponse of(final ErrorCode errorCode,
        final String missingParameterName) {
        return new ErrorResponse(errorCode,
            FieldError.of(missingParameterName, "", errorCode.getMessage()));
    }

    // 에러코드만 전달(주로 커스텀익셉션)
    public static ErrorResponse of(final ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    // 에러코드 + 필드 목록
    public static ErrorResponse of(final ErrorCode errorCode, final List<FieldError> fieldErrors) {
        return new ErrorResponse(errorCode, fieldErrors);
    }

    // 잘못된 HTTP 메소드 타입
    public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
        String value = e.getValue() == null ? "" : e.getValue().toString();
        return new ErrorResponse(CommonErrorCode.INVALID_TYPE_VALUE,
            FieldError.of(e.getName(), value, CommonErrorCode.INVALID_TYPE_VALUE.getMessage()));
    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {

        private String field;
        private String value;
        private String reason;

        public FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(String field, String value, String reason) {
            List<FieldError> list = new ArrayList<>();
            list.add(new FieldError(field, value, reason));
            return list;
        }

        public static List<FieldError> of(BindingResult bindingResult) {
            return bindingResult.getFieldErrors().stream()
                .map(error -> new FieldError(
                    error.getField(),
                    error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                    error.getDefaultMessage()))
                .collect(Collectors.toList());
        }

        public static List<FieldError> of(Set<ConstraintViolation<?>> violations) {
            return violations.stream()
                .map(v -> new FieldError(
                    v.getPropertyPath().toString(),
                    "",
                    v.getMessage()))
                .collect(Collectors.toList());
        }
    }
}