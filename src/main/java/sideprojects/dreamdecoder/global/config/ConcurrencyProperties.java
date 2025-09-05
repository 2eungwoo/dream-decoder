package sideprojects.dreamdecoder.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "concurrency")
public class ConcurrencyProperties {

    private int permits;
    private final DuplicateRequestLock duplicateRequestLock = new DuplicateRequestLock();
    private final Semaphore semaphore = new Semaphore();

    @Getter
    @Setter
    public static class DuplicateRequestLock {
        private String keyPrefix;
        private Duration ttl;
    }

    @Getter
    @Setter
    public static class Semaphore {
        private String key;
        private long waitTimeSeconds;
    }
}
