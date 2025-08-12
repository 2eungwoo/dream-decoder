package sideprojects.dreamdecoder.domain.dream.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DreamType {
    // 운세 (FORTUNE)
    JOB(DreamCategory.FORTUNE, "취업·직장운"),
    PROMOTION(DreamCategory.FORTUNE, "승진·평판운"),
    LOVE_FORTUNE(DreamCategory.FORTUNE, "연애·결혼운"),
    WEALTH_FORTUNE(DreamCategory.FORTUNE, "금전·재물운"),
    BUSINESS_FORTUNE(DreamCategory.FORTUNE, "사업·운수"),
    EXAM_FORTUNE(DreamCategory.FORTUNE, "시험·합격운"),
    HEALTH_FORTUNE(DreamCategory.FORTUNE, "건강운"),
    LUCK_GENERAL(DreamCategory.FORTUNE, "전반적 운세"),
    MUNDANE(DreamCategory.FORTUNE, "개꿈(무의미)"),

    // 재물/금전 (WEALTH)
    FIND_MONEY(DreamCategory.WEALTH, "돈을 줍는 꿈"),
    LOTTERY(DreamCategory.WEALTH, "복권에 당첨되는 꿈"),
    LOSE_MONEY(DreamCategory.WEALTH, "돈을 잃어버리는 꿈"),
    GOLD(DreamCategory.WEALTH, "금·보석을 얻는 꿈"),

    // 연애/대인관계 (RELATIONSHIP)
    NEW_LOVE(DreamCategory.RELATIONSHIP, "새로운 사랑"),
    EX_PARTNER(DreamCategory.RELATIONSHIP, "옛 연인"),
    MARRIAGE(DreamCategory.RELATIONSHIP, "결혼/약혼"),
    ARGUMENT(DreamCategory.RELATIONSHIP, "다툼/갈등"),

    // 취업/학업/성공 (CAREER)
    JOB_OFFER(DreamCategory.CAREER, "입사 제안"),
    INTERVIEW(DreamCategory.CAREER, "면접"),
    EXAM_PASS(DreamCategory.CAREER, "시험 합격"),
    AWARD(DreamCategory.CAREER, "수상/인정받음"),

    // 건강/컨디션 (HEALTH)
    HEALING(DreamCategory.HEALTH, "병이 나음"),
    ILLNESS(DreamCategory.HEALTH, "아픈 꿈"),
    EXERCISE(DreamCategory.HEALTH, "운동하는 꿈"),

    // 가정/출산/집 (FAMILY_HOME)
    BABY_BIRTH(DreamCategory.FAMILY_HOME, "아기를 낳는 꿈"),
    BABY_HOLD(DreamCategory.FAMILY_HOME, "아기를 안는 꿈"),
    NEW_HOUSE(DreamCategory.FAMILY_HOME, "새 집으로 이사"),
    HOUSE_COLLAPSE(DreamCategory.FAMILY_HOME, "집이 무너지는 꿈"),

    // 여행/이동 (TRAVEL)
    FLYING(DreamCategory.TRAVEL, "하늘을 나는 꿈"),
    DRIVING(DreamCategory.TRAVEL, "차를 운전하는 꿈"),
    TRAIN_TRAVEL(DreamCategory.TRAVEL, "기차/지하철로 이동"),
    FLIGHT_DELAY(DreamCategory.TRAVEL, "비행기 지연"),

    // 자연/동물 (NATURE_ANIMALS)
    TIGER(DreamCategory.NATURE_ANIMALS, "호랑이"),
    SNAKE(DreamCategory.NATURE_ANIMALS, "뱀"),
    PIG(DreamCategory.NATURE_ANIMALS, "돼지"),
    DOG(DreamCategory.NATURE_ANIMALS, "개"),
    CAT(DreamCategory.NATURE_ANIMALS, "고양이"),
    FISH(DreamCategory.NATURE_ANIMALS, "물고기"),
    BIRD(DreamCategory.NATURE_ANIMALS, "새"),
    INSECT(DreamCategory.NATURE_ANIMALS, "곤충"),

    // 물/불/자연현상 (WATER_FIRE)
    CLEAR_WATER(DreamCategory.WATER_FIRE, "맑은 물"),
    FLOOD(DreamCategory.WATER_FIRE, "홍수/범람"),
    FIRE(DreamCategory.WATER_FIRE, "불이 나는 꿈"),
    RAINBOW(DreamCategory.WATER_FIRE, "무지개"),

    // 초자연/영적 (SUPERNATURAL)
    DECEASED_VISIT(DreamCategory.SUPERNATURAL, "돌아가신 분을 만남"),
    GHOST(DreamCategory.SUPERNATURAL, "귀신"),
    PROPHECY(DreamCategory.SUPERNATURAL, "예지몽"),
    DEITY(DreamCategory.SUPERNATURAL, "신/성스러운 존재"),

    // 감정/심리 (EMOTION)
    JOY(DreamCategory.EMOTION, "기쁨"),
    FEAR(DreamCategory.EMOTION, "불안"),
    CONFUSION(DreamCategory.EMOTION, "혼란"),
    STRESS(DreamCategory.EMOTION, "스트레스"),

    // 위험/추격/갈등 (DANGER)
    CHASED(DreamCategory.DANGER, "쫓기는 꿈"),
    FALLING(DreamCategory.DANGER, "떨어지는 꿈"),
    FIGHT(DreamCategory.DANGER, "싸우는 꿈"),

    // 상실/변화 (LOSS_CHANGE)
    TEETH_FALL(DreamCategory.LOSS_CHANGE, "이빨이 빠짐"),
    HAIR_CUT(DreamCategory.LOSS_CHANGE, "머리카락을 자름"),
    BREAKUP(DreamCategory.LOSS_CHANGE, "이별"),

    // 사물/음식/의복 (OBJECTS)
    FOOD_FEAST(DreamCategory.OBJECTS, "맛있는 음식을 먹음"),
    CLOTHES_NEW(DreamCategory.OBJECTS, "새 옷"),
    RING_FOUND(DreamCategory.OBJECTS, "반지를 줍는 꿈"),

    // 사회/공공장소 (SOCIAL_PUBLIC)
    SCHOOL(DreamCategory.SOCIAL_PUBLIC, "학교에 있는 꿈"),
    MARKET(DreamCategory.SOCIAL_PUBLIC, "시장/상점"),
    HOSPITAL(DreamCategory.SOCIAL_PUBLIC, "병원"),

    // 징조/예지 (DREAM_SIGNS)
    SUNRISE(DreamCategory.DREAM_SIGNS, "해돋이"),
    FULL_MOON(DreamCategory.DREAM_SIGNS, "보름달"),
    STAR_FALL(DreamCategory.DREAM_SIGNS, "별똥별");

    private final DreamCategory category;
    private final String description;
}
