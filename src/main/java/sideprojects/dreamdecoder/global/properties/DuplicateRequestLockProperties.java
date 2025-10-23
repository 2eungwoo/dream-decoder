package sideprojects.dreamdecoder.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "concurrency.duplicate-request-lock")
public class DuplicateRequestLockProperties {

    private String keyPrefix;
    private Duration ttl;
}
