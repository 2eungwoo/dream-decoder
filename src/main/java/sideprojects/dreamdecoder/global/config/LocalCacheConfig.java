package sideprojects.dreamdecoder.global.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.List;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
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

    @Bean(name = "localCacheManager")
    public CacheManager localCacheManager(Caffeine<Object, Object> localCacheBuilder) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(localCacheBuilder);
        cacheManager.setCacheNames(List.of("dreamInterpretations", "dreamSymbols"));
        return cacheManager;
    }
}
