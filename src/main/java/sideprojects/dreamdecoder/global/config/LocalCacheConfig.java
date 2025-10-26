package sideprojects.dreamdecoder.global.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sideprojects.dreamdecoder.global.properties.LocalCacheProperties;

@Configuration
public class LocalCacheConfig {

    @Bean
    public Caffeine<Object, Object> localCacheBuilder(LocalCacheProperties properties) {
        return Caffeine.newBuilder()
            .initialCapacity(properties.getInitialCapacity())
            .maximumSize(properties.getMaximumSize())
            .expireAfterWrite(properties.getExpireAfterWrite());
    }

    @Bean(name = "localCache")
    public Cache<String, Object> localCache(Caffeine<Object, Object> localCacheBuilder) {
        return localCacheBuilder.build();
    }
}
