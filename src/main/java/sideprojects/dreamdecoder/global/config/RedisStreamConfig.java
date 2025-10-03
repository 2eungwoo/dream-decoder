package sideprojects.dreamdecoder.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class RedisStreamConfig {

    @Value("${redis.stream.key:dream-save-jobs}")
    private String streamKey;

    @Value("${redis.stream.group:dream-savers}")
    private String groupName;

    @Value("${redis.stream.consumer:consumer-1}")
    private String consumerName;
}
