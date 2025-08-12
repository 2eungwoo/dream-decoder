package sideprojects.dreamdecoder.presentation.dream.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DreamInterpretationRequest {

    @NotBlank(message = "꿈 내용은 비워둘 수 없습니다.")
    private String dreamContent;

    private AiStyle style;
}
