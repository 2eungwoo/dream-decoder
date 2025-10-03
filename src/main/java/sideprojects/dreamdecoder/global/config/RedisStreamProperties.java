package sideprojects.dreamdecoder.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.data.redis.stream")
public class RedisStreamProperties {

    private String key;
    private String group;
    private String consumerPrefix;
}
