package sideprojects.dreamdecoder.application.web.dream.producer;

import lombok.Builder;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

import java.util.List;

@Builder
public record DreamSaveJobCommand(
    Long userId,
    String idempotencyKey,
    String dreamContent,
    String interpretation,
    DreamEmotion dreamEmotion,
    String tags,
    AiStyle style,
    List<DreamType> types
) {
}
