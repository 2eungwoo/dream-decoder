package sideprojects.dreamdecoder.domain.dream.util.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DreamCategory {
    FORTUNE("운세"),
    WEALTH("재물/금전"),
    RELATIONSHIP("연애/대인관계"),
    CAREER("취업/학업/성공"),
    HEALTH("건강/컨디션"),
    FAMILY_HOME("가정/출산/집"),
    TRAVEL("여행/이동"),
    NATURE_ANIMALS("자연/동물"),
    WATER_FIRE("물/불/자연현상"),
    SUPERNATURAL("초자연/영적"),
    EMOTION("감정/심리"),
    DANGER("위험/추격/갈등"),
    LOSS_CHANGE("상실/변화"),
    OBJECTS("사물/음식/의복"),
    SOCIAL_PUBLIC("사회/공공장소"),
    DREAM_SIGNS("징조/예지"),
    NONE("없음");


    private final String description;
}