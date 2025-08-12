package sideprojects.dreamdecoder.domain.dream.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DreamType {
    // 운세/결과 (FORTUNE)
    JOB(DreamCategory.FORTUNE, "취업·직장운"),
    PROMOTION(DreamCategory.FORTUNE, "승진·평판운"),
    LOVE(DreamCategory.FORTUNE, "연애·결혼운"),
    WEALTH(DreamCategory.FORTUNE, "금전·재물운"),
    BUSINESS(DreamCategory.FORTUNE, "사업·운수"),
    EXAM(DreamCategory.FORTUNE, "시험·합격운"),
    HEALTH(DreamCategory.FORTUNE, "건강운"),
    LUCK_GENERAL(DreamCategory.FORTUNE, "전반적 운세"),
    MUNDANE(DreamCategory.FORTUNE, "개꿈(무의미)"),

    // 심리/감정 (EMOTION)
    FEAR(DreamCategory.EMOTION, "불안·공포"),
    STRESS(DreamCategory.EMOTION, "스트레스·압박"),
    GUILT(DreamCategory.EMOTION, "죄책감"),
    HAPPINESS(DreamCategory.EMOTION, "행복·기쁨"),
    HOPE(DreamCategory.EMOTION, "희망·영감"),
    CONFUSION(DreamCategory.EMOTION, "혼란·애매함"),

    // 인간관계 (RELATION)
    FAMILY(DreamCategory.RELATION, "가족·가정사"),
    FRIENDSHIP(DreamCategory.RELATION, "친구·동료"),
    AUTHORITY(DreamCategory.RELATION, "상사·권위자"),
    STRANGER(DreamCategory.RELATION, "낯선 사람"),
    EX_PARTNER(DreamCategory.RELATION, "옛 연인"),

    // 자연/동물 (ANIMAL_NATURE)
    TIGER(DreamCategory.ANIMAL_NATURE, "호랑이"),
    SNAKE(DreamCategory.ANIMAL_NATURE, "뱀"),
    PIG(DreamCategory.ANIMAL_NATURE, "돼지"),
    DOG(DreamCategory.ANIMAL_NATURE, "개"),
    CAT(DreamCategory.ANIMAL_NATURE, "고양이"),
    FISH(DreamCategory.ANIMAL_NATURE, "물고기"),
    BIRD(DreamCategory.ANIMAL_NATURE, "새"),
    INSECT(DreamCategory.ANIMAL_NATURE, "곤충"),
    WATER(DreamCategory.ANIMAL_NATURE, "물/바다/강"),
    FIRE(DreamCategory.ANIMAL_NATURE, "불/화재"),
    RAIN(DreamCategory.ANIMAL_NATURE, "비/폭우"),
    MOUNTAIN(DreamCategory.ANIMAL_NATURE, "산/절벽"),
    SKY(DreamCategory.ANIMAL_NATURE, "하늘/우주"),

    // 사물/사건 (OBJECT_EVENT)
    MONEY(DreamCategory.OBJECT_EVENT, "돈/지폐/동전"),
    RING(DreamCategory.OBJECT_EVENT, "반지/장신구"),
    HOUSE(DreamCategory.OBJECT_EVENT, "집/부동산"),
    CAR(DreamCategory.OBJECT_EVENT, "차/교통"),
    PHONE(DreamCategory.OBJECT_EVENT, "휴대폰/통신"),
    SHOES(DreamCategory.OBJECT_EVENT, "신발"),
    BIRTH(DreamCategory.OBJECT_EVENT, "출산"),
    WEDDING(DreamCategory.OBJECT_EVENT, "결혼식"),
    FUNERAL(DreamCategory.OBJECT_EVENT, "장례식"),
    ACCIDENT(DreamCategory.OBJECT_EVENT, "사고/파손"),
    LOST_FOUND(DreamCategory.OBJECT_EVENT, "분실/습득"),
    TEETH(DreamCategory.OBJECT_EVENT, "이빨/치아"),
    HAIR(DreamCategory.OBJECT_EVENT, "머리카락"),

    // 상징/징조 (SYMBOL_OMEN)
    FLYING(DreamCategory.SYMBOL_OMEN, "비행/날기"),
    FALLING(DreamCategory.SYMBOL_OMEN, "추락"),
    CHASE(DreamCategory.SYMBOL_OMEN, "추격"),
    NAKED(DreamCategory.SYMBOL_OMEN, "알몸/노출"),
    DOOR(DreamCategory.SYMBOL_OMEN, "문/통로"),
    BRIDGE(DreamCategory.SYMBOL_OMEN, "다리/건넘"),
    KEYS(DreamCategory.SYMBOL_OMEN, "열쇠/잠금"),
    BLOOD(DreamCategory.SYMBOL_OMEN, "피/상처"),
    LIGHT(DreamCategory.SYMBOL_OMEN, "빛/계시"),

    // 악몽/위협 (NIGHTMARE)
    MONSTER(DreamCategory.NIGHTMARE, "괴물/위협 존재"),
    PARALYSIS(DreamCategory.NIGHTMARE, "가위눌림"),
    DROWNING(DreamCategory.NIGHTMARE, "익사/숨막힘"),
    WAR(DreamCategory.NIGHTMARE, "전쟁/폭력"),
    BREAKUP(DreamCategory.NIGHTMARE, "이별/배신"),

    // 특수/기타 (SPECIAL)
    LUCID(DreamCategory.SPECIAL, "자각몽"),
    PROPHECY(DreamCategory.SPECIAL, "예지몽"),
    DEJA_VU(DreamCategory.SPECIAL, "데자뷰"),
    SPIRIT(DreamCategory.SPECIAL, "영가/귀신"),
    DEITY(DreamCategory.SPECIAL, "신/성스러운 존재"),
    ANCESTOR(DreamCategory.SPECIAL, "조상꿈");

    private final DreamCategory category;
    private final String description;
}
