package sideprojects.dreamdecoder.application.dream.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.presentation.dream.dto.request.DreamInterpretationRequest;
import sideprojects.dreamdecoder.presentation.dream.dto.response.DreamInterpretationResponse;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InterpretationCacheService {

    private static final String CACHE_KEY_PREFIX = "interpretation:cache:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(10);

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
            // TODO: 적절한 예외 처리 필요 (e.g., CustomException)
            throw new IllegalArgumentException("유효하지 않거나 만료된 ID입니다.");
        }

        redisTemplate.delete(cacheKey);
        return (Map<String, Object>) cachedData;
    }
}
