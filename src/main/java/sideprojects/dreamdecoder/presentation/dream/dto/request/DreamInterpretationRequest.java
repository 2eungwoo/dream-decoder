package sideprojects.dreamdecoder.presentation.dream.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DreamInterpretationRequest {

    @NotBlank(message = "꿈 내용은 비워둘 수 없습니다.")
    private String dreamContent;
}
