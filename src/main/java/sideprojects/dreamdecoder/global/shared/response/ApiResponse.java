package sideprojects.dreamdecoder.global.shared.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ApiResponse<T> {

    private final HttpStatus status;
    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final T data;

    public ApiResponse(ResponseCode code, T data) {
        this.status = code.getStatus();
        this.code = code.getCode();
        this.message = code.getMessage();
        this.data = data;
    }

    public ApiResponse(ResponseCode code) {
        this(code, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(
        ResponseCode responseCode, T data) {
        return ResponseEntity
            .status(responseCode.getStatus())
            .body(new ApiResponse<>(responseCode, data));
    }

    public static ResponseEntity<ApiResponse<Void>> success(
        ResponseCode responseCode) {
        return ResponseEntity
            .status(responseCode.getStatus())
            .body(new ApiResponse<>(responseCode));
    }
}