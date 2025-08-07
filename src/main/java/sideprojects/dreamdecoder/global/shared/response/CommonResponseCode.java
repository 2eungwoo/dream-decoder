package sideprojects.dreamdecoder.global.shared.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonResponseCode implements ResponseCode {
    OK(HttpStatus.OK, "R001", "Success");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
