package sideprojects.dreamdecoder.infrastructure.external.openai.util;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InterpretationCacheManager {

    private static final String CACHE_KEY_PREFIX = "interpretation_cache:";
    private static final long CACHE_TTL_HOURS = 1;
    private final RedissonClient redissonClient;

    public Optional<String> get(Long userId, String dreamContent, DreamEmotion dreamEmotion, String tags, AiStyle style) {
        String cacheKey = createCacheKey(userId, dreamContent, dreamEmotion, tags, style);
        RBucket<String> bucket = redissonClient.getBucket(cacheKey);
        return Optional.ofNullable(bucket.get());
    }

    public void set(Long userId, String dreamContent, DreamEmotion dreamEmotion, String tags, AiStyle style, String interpretation) {
        String cacheKey = createCacheKey(userId, dreamContent, dreamEmotion, tags, style);
        RBucket<String> bucket = redissonClient.getBucket(cacheKey);
        bucket.set(interpretation, Duration.ofHours(CACHE_TTL_HOURS));
    }

    private String createCacheKey(Long userId, String dreamContent, DreamEmotion dreamEmotion, String tags, AiStyle style) {
        int hashCode = Objects.hash(userId, dreamContent, dreamEmotion, tags, style);
        return CACHE_KEY_PREFIX + hashCode;
    }
}
