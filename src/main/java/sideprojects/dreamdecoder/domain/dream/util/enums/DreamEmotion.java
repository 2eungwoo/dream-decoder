package sideprojects.dreamdecoder.domain.dream.util.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DreamEmotion {
    HAPPY("행복"),
    ANXIOUS("불안"),
    SAD("슬픔"),
    EXCITED("신남"),
    CALM("평온"),
    FEAR("두려움"),
    CONFUSED("혼란"),
    NEUTRAL("무덤덤");

    private final String description;
}
