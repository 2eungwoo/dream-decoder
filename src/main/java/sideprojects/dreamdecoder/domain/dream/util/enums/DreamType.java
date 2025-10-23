package sideprojects.dreamdecoder.domain.dream.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DreamType {
    // 운세 (FORTUNE)
    JOB(DreamCategory.FORTUNE, DreamOutcome.GOOD, "취업·직장운"),
    PROMOTION(DreamCategory.FORTUNE, DreamOutcome.GOOD, "승진·평판운"),
    LOVE_FORTUNE(DreamCategory.FORTUNE, DreamOutcome.GOOD, "연애·결혼운"),
    WEALTH_FORTUNE(DreamCategory.FORTUNE, DreamOutcome.VERY_GOOD, "금전·재물운"),
    BUSINESS_FORTUNE(DreamCategory.FORTUNE, DreamOutcome.GOOD, "사업·운수"),
    EXAM_FORTUNE(DreamCategory.FORTUNE, DreamOutcome.GOOD, "시험·합격운"),
    HEALTH_FORTUNE(DreamCategory.FORTUNE, DreamOutcome.NEUTRAL, "건강운"),
    LUCK_GENERAL(DreamCategory.FORTUNE, DreamOutcome.NEUTRAL, "전반적 운세"),
    MUNDANE(DreamCategory.FORTUNE, DreamOutcome.NEUTRAL, "개꿈(무의미)"),

    // 재물/금전 (WEALTH)
    FIND_MONEY(DreamCategory.WEALTH, DreamOutcome.GOOD, "돈을 줍는 꿈"),
    LOTTERY(DreamCategory.WEALTH, DreamOutcome.VERY_GOOD, "복권에 당첨되는 꿈"),
    LOSE_MONEY(DreamCategory.WEALTH, DreamOutcome.BAD, "돈을 잃어버리는 꿈"),
    GOLD(DreamCategory.WEALTH, DreamOutcome.VERY_GOOD, "금·보석을 얻는 꿈"),

    // 연애/대인관계 (RELATIONSHIP)
    NEW_LOVE(DreamCategory.RELATIONSHIP, DreamOutcome.GOOD, "새로운 사랑"),
    EX_PARTNER(DreamCategory.RELATIONSHIP, DreamOutcome.NEUTRAL, "옛 연인"),
    MARRIAGE(DreamCategory.RELATIONSHIP, DreamOutcome.VERY_GOOD, "결혼/약혼"),
    ARGUMENT(DreamCategory.RELATIONSHIP, DreamOutcome.WARNING, "다툼/갈등"),
    FRIEND(DreamCategory.RELATIONSHIP, DreamOutcome.NEUTRAL, "친구"),

    // 취업/학업/성공 (CAREER)
    JOB_OFFER(DreamCategory.CAREER, DreamOutcome.VERY_GOOD, "입사 제안"),
    INTERVIEW(DreamCategory.CAREER, DreamOutcome.NEUTRAL, "면접"),
    EXAM_PASS(DreamCategory.CAREER, DreamOutcome.VERY_GOOD, "시험 합격"),
    AWARD(DreamCategory.CAREER, DreamOutcome.VERY_GOOD, "수상/인정받음"),

    // 건강/컨디션 (HEALTH)
    HEALING(DreamCategory.HEALTH, DreamOutcome.GOOD, "병이 나음"),
    ILLNESS(DreamCategory.HEALTH, DreamOutcome.BAD, "아픈 꿈"),
    EXERCISE(DreamCategory.HEALTH, DreamOutcome.NEUTRAL, "운동하는 꿈"),

    // 가정/출산/집 (FAMILY_HOME)
    BABY_BIRTH(DreamCategory.FAMILY_HOME, DreamOutcome.VERY_GOOD, "아기를 낳는 꿈"),
    BABY_HOLD(DreamCategory.FAMILY_HOME, DreamOutcome.GOOD, "아기를 안는 꿈"),
    NEW_HOUSE(DreamCategory.FAMILY_HOME, DreamOutcome.GOOD, "새 집으로 이사"),
    HOUSE_COLLAPSE(DreamCategory.FAMILY_HOME, DreamOutcome.VERY_BAD, "집이 무너지는 꿈"),

    // 여행/이동 (TRAVEL)
    FLYING(DreamCategory.TRAVEL, DreamOutcome.OPPORTUNITY, "하늘을 나는 꿈"),
    DRIVING(DreamCategory.TRAVEL, DreamOutcome.NEUTRAL, "차를 운전하는 꿈"),
    TRAIN_TRAVEL(DreamCategory.TRAVEL, DreamOutcome.NEUTRAL, "기차/지하철로 이동"),
    FLIGHT_DELAY(DreamCategory.TRAVEL, DreamOutcome.WARNING, "비행기 지연"),

    // 자연/동물 (NATURE_ANIMALS)
    TIGER(DreamCategory.NATURE_ANIMALS, DreamOutcome.GOOD, "호랑이"),
    SNAKE(DreamCategory.NATURE_ANIMALS, DreamOutcome.GOOD, "뱀"),
    PIG(DreamCategory.NATURE_ANIMALS, DreamOutcome.VERY_GOOD, "돼지"),
    DOG(DreamCategory.NATURE_ANIMALS, DreamOutcome.NEUTRAL, "개"),
    CAT(DreamCategory.NATURE_ANIMALS, DreamOutcome.NEUTRAL, "고양이"),
    FISH(DreamCategory.NATURE_ANIMALS, DreamOutcome.GOOD, "물고기"),
    BIRD(DreamCategory.NATURE_ANIMALS, DreamOutcome.NEUTRAL, "새"),
    INSECT(DreamCategory.NATURE_ANIMALS, DreamOutcome.NEUTRAL, "곤충"),

    // 물/불/자연현상 (WATER_FIRE)
    CLEAR_WATER(DreamCategory.WATER_FIRE, DreamOutcome.GOOD, "맑은 물"),
    FLOOD(DreamCategory.WATER_FIRE, DreamOutcome.WARNING, "홍수/범람"),
    FIRE(DreamCategory.WATER_FIRE, DreamOutcome.VERY_GOOD, "불이 나는 꿈"),
    RAINBOW(DreamCategory.WATER_FIRE, DreamOutcome.VERY_GOOD, "무지개"),

    // 초자연/영적 (SUPERNATURAL)
    DECEASED_VISIT(DreamCategory.SUPERNATURAL, DreamOutcome.NEUTRAL, "돌아가신 분을 만남"),
    GHOST(DreamCategory.SUPERNATURAL, DreamOutcome.BAD, "귀신"),
    PROPHECY(DreamCategory.SUPERNATURAL, DreamOutcome.OPPORTUNITY, "예지몽"),
    DEITY(DreamCategory.SUPERNATURAL, DreamOutcome.VERY_GOOD, "신/성스러운 존재"),

    // 감정/심리 (EMOTION)
    JOY(DreamCategory.EMOTION, DreamOutcome.GOOD, "기쁨"),
    FEAR(DreamCategory.EMOTION, DreamOutcome.BAD, "불안"),
    CONFUSION(DreamCategory.EMOTION, DreamOutcome.WARNING, "혼란"),
    STRESS(DreamCategory.EMOTION, DreamOutcome.BAD, "스트레스"),

    // 위험/추격/갈등 (DANGER)
    CHASED(DreamCategory.DANGER, DreamOutcome.WARNING, "쫓기는 꿈"),
    FALLING(DreamCategory.DANGER, DreamOutcome.WARNING, "떨어지는 꿈"),
    FIGHT(DreamCategory.DANGER, DreamOutcome.BAD, "싸우는 꿈"),

    // 상실/변화 (LOSS_CHANGE)
    TEETH_FALL(DreamCategory.LOSS_CHANGE, DreamOutcome.VERY_BAD, "이빨이 빠짐"),
    HAIR_CUT(DreamCategory.LOSS_CHANGE, DreamOutcome.WARNING, "머리카락을 자름"),
    BREAKUP(DreamCategory.LOSS_CHANGE, DreamOutcome.BAD, "이별"),

    // 사물/음식/의복 (OBJECTS)
    FOOD_FEAST(DreamCategory.OBJECTS, DreamOutcome.GOOD, "맛있는 음식을 먹음"),
    CLOTHES_NEW(DreamCategory.OBJECTS, DreamOutcome.GOOD, "새 옷"),
    RING_FOUND(DreamCategory.OBJECTS, DreamOutcome.GOOD, "반지를 줍는 꿈"),

    // 사회/공공장소 (SOCIAL_PUBLIC)
    SCHOOL(DreamCategory.SOCIAL_PUBLIC, DreamOutcome.NEUTRAL, "학교에 있는 꿈"),
    MARKET(DreamCategory.SOCIAL_PUBLIC, DreamOutcome.NEUTRAL, "시장/상점"),
    HOSPITAL(DreamCategory.SOCIAL_PUBLIC, DreamOutcome.WARNING, "병원"),

    // 징조/예지 (DREAM_SIGNS)
    SUNRISE(DreamCategory.DREAM_SIGNS, DreamOutcome.VERY_GOOD, "해돋이"),
    FULL_MOON(DreamCategory.DREAM_SIGNS, DreamOutcome.GOOD, "보름달"),
    STAR_FALL(DreamCategory.DREAM_SIGNS, DreamOutcome.OPPORTUNITY, "별똥별"),

    // 추출 심볼 없는 경우
    NONE(DreamCategory.NONE, DreamOutcome.NONE, "없음");

    private final DreamCategory category;
    private final DreamOutcome outcome;
    private final String description;
}
