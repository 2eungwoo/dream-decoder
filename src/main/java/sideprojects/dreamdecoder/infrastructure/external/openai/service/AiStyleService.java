package sideprojects.dreamdecoder.infrastructure.external.openai.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class AiStyleService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String STYLE_PREFIX = "ai_style:";
    private static final Duration STYLE_TTL = Duration.ofHours(1); // 1시간 TTL

    public AiStyleService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setStyle(Long userId, String style) {
        String key = STYLE_PREFIX + userId;
        redisTemplate.opsForValue().set(key, style, STYLE_TTL);
    }

    public String getStyle(Long userId) {
        String key = STYLE_PREFIX + userId;
        String style = redisTemplate.opsForValue().get(key);
        return style != null ? style : "DEFAULT";
    }
}