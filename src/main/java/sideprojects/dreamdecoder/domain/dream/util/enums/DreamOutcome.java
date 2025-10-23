package sideprojects.dreamdecoder.domain.dream.util.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DreamOutcome {
    VERY_GOOD("아주 좋은 징조"),
    GOOD("좋은 징조"),
    NEUTRAL("보통/애매한 징조"),
    BAD("나쁜 징조"),
    VERY_BAD("아주 나쁜 징조"),
    WARNING("주의/경고 필요"),
    OPPORTUNITY("기회/전환점"),
    NONE("없음");

    private final String description;
}