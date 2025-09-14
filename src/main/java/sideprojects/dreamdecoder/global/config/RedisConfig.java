package sideprojects.dreamdecoder.global.config;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

@EnableCaching
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory cf) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                new Jackson2JsonRedisSerializer<>(Object.class)))
            .entryTtl(Duration.ofHours(24)); // Set TTL to 24 hours

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(cf)
            .cacheDefaults(redisCacheConfiguration).build();
    }

    @Bean("dreamInterpretationKeyGenerator")
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            // params[0]: AiStyle style
            // params[1]: DreamEmotion dreamEmotion
            // params[2]: List<DreamType> extractedTypes
            // params[3]: String dreamContent

            AiStyle style = (AiStyle) params[0];
            DreamEmotion emotion = (DreamEmotion) params[1];
            List<DreamType> types = (List<DreamType>) params[2];

            String sortedSymbolTypes = types.stream()
                .map(DreamType::name)
                .sorted()
                .collect(Collectors.joining(","));

            return "interpretation:" + style.name() + ":" + emotion.name() + ":"
                + sortedSymbolTypes;
        };
    }

}
