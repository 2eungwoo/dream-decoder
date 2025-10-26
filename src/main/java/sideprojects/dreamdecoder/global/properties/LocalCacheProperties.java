package sideprojects.dreamdecoder.global.properties;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "local-cache")
public class LocalCacheProperties {

    private int initialCapacity;
    private long maximumSize;
    private Duration expireAfterWrite;
}
