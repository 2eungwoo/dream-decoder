package sideprojects.dreamdecoder.infrastructure.external.openai.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenAiClient {

    private final OpenAiProperties properties;
    private final RestTemplate restTemplate = new RestTemplate();

    public String chat(String systemPrompt, String userPrompt) {
        // 요청 바디 구성
        Map<String, Object> body = Map.of(
            "model", properties.getModel(),
            "messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userPrompt)
            )
        );

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getApiKey());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                properties.getApiUrl(),
                HttpMethod.POST,
                request,
                Map.class
            );

            // 응답 파싱
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            if (choices == null || choices.isEmpty()) {
                throw new RuntimeException("No choices returned from OpenAI");
            }

            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");

        } catch (HttpStatusCodeException ex) {
            throw new RuntimeException("OpenAI API request failed: " + ex.getResponseBodyAsString(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error during OpenAI API call", ex);
        }
    }
}