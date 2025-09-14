package sideprojects.dreamdecoder.infrastructure.external.openai.util;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class InterpretationCacheManager {

    private static final String CACHE_KEY_PREFIX = "interpretation_cache:";
    private static final long CACHE_TTL_HOURS = 1;
    private final RedissonClient redissonClient;

    public Optional<String> get(Long userId, String dreamContent) {
        String cacheKey = createCacheKey(userId, dreamContent);
        RBucket<String> bucket = redissonClient.getBucket(cacheKey);
        return Optional.ofNullable(bucket.get());
    }

    public void set(Long userId, String dreamContent, String interpretation) {
        String cacheKey = createCacheKey(userId, dreamContent);
        RBucket<String> bucket = redissonClient.getBucket(cacheKey);
        bucket.set(interpretation, CACHE_TTL_HOURS, TimeUnit.HOURS);
    }

    private String createCacheKey(Long userId, String dreamContent) {
        return CACHE_KEY_PREFIX + userId + ":" + dreamContent.hashCode();
    }
}
