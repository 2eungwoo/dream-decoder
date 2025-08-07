package sideprojects.dreamdecoder.infrastructure.external.openai.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

import java.time.Duration;

@Service
public class AiStyleService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String STYLE_PREFIX = "ai_style:";
    private static final Duration STYLE_TTL = Duration.ofHours(1); // 1시간 TTL

    public AiStyleService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setStyle(Long userId, AiStyle style) {
        String key = STYLE_PREFIX + userId;
        redisTemplate.opsForValue().set(key, style.name(), STYLE_TTL);
    }

    public AiStyle getStyle(Long userId) {
        String key = STYLE_PREFIX + userId;
        String styleName = redisTemplate.opsForValue().get(key);
        return styleName != null ? AiStyle.valueOf(styleName) : AiStyle.DEFAULT;
    }
}