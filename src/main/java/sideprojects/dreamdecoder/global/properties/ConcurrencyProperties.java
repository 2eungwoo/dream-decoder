package sideprojects.dreamdecoder.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "concurrency")
public class ConcurrencyProperties {

    private int permits;
}