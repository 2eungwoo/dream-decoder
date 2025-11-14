package sideprojects.dreamdecoder.presentation.web.dream.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DreamInterpretationRequest {

    @NotBlank(message = "꿈 내용은 비워둘 수 없습니다.")
    @Size(max = 500, message = "꿈 내용은 최대 500자까지 입력할 수 있습니다.")
    private String dreamContent;

    private DreamEmotion dreamEmotion;
    private String tags;

    private AiStyle style;
}
