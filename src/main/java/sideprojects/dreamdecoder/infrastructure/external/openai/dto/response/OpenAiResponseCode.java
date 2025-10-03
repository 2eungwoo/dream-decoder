package sideprojects.dreamdecoder.infrastructure.external.openai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import sideprojects.dreamdecoder.global.shared.response.ResponseCode;

@Getter
@AllArgsConstructor
public enum OpenAiResponseCode implements ResponseCode {
    AI_STYLE_SET_SUCCESS(HttpStatus.OK, "OAI_R001", "AI 스타일이 성공적으로 설정되었습니다."),
    AI_CHAT_SUCCESS(HttpStatus.OK, "OA001", "AI 해몽에 성공했습니다."),
    AI_CHAT_REQUEST_SUCCESS(HttpStatus.ACCEPTED, "OA002", "AI 해몽 요청에 성공했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
