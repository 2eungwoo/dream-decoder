package sideprojects.dreamdecoder.global.config;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean("dreamInterpretationKeyGenerator")
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
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
