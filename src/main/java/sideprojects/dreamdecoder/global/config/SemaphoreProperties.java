package sideprojects.dreamdecoder.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "concurrency.semaphore")
public class SemaphoreProperties {

    private String key;
    private long waitTimeSeconds;
}
