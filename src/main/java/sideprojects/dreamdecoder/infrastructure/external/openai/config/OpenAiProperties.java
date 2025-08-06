package sideprojects.dreamdecoder.infrastructure.external.openai.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class OpenAiProperties {

    @Value("${openai.api-key}")
    public String apiKey;

    @Value("${openai.api-url}")
    public String apiUrl;

    @Value("${openai.model}")
    public String model;
}