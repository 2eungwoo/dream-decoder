package sideprojects.dreamdecoder.application.dream.service.interpreter;

import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

import java.util.Map;

public record DreamJobPayload(Long userId,
                              String dreamContent,
                              DreamEmotion dreamEmotion,
                              AiStyle style,
                              String tags) {

}
