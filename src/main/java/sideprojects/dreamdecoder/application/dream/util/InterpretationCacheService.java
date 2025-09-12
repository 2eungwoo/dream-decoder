package sideprojects.dreamdecoder.application.dream.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.presentation.dream.dto.request.DreamInterpretationRequest;
import sideprojects.dreamdecoder.presentation.dream.dto.response.DreamInterpretationResponse;

import sideprojects.dreamdecoder.application.dream.util.exception.NoCacheDataException;
import sideprojects.dreamdecoder.application.dream.util.exception.CacheErrorCode;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InterpretationCacheService {

    private static final String CACHE_KEY_PREFIX = "interpretation:cache:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(2);

    private final RedisTemplate<String, Object> redisTemplate;

    public String cacheInterpretation(DreamInterpretationRequest request, DreamInterpretationResponse response) {
        String interpretationId = UUID.randomUUID().toString();
        String cacheKey = CACHE_KEY_PREFIX + interpretationId;

        Map<String, Object> context = Map.of(
                "request", request,
                "response", response
        );

        redisTemplate.opsForValue().set(cacheKey, context, CACHE_TTL);
        return interpretationId;
    }

    public Map<String, Object> getAndRemoveCachedInterpretation(String interpretationId) {
        String cacheKey = CACHE_KEY_PREFIX + interpretationId;
        Object cachedData = redisTemplate.opsForValue().get(cacheKey);

        if (cachedData == null) {
            throw new NoCacheDataException(CacheErrorCode.CACHE_DATA_NOT_FOUND);
        }

        redisTemplate.delete(cacheKey);
        return (Map<String, Object>) cachedData;
    }
}
