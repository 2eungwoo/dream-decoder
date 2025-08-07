package sideprojects.dreamdecoder.infrastructure.external.openai.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import sideprojects.dreamdecoder.infrastructure.external.openai.dto.OpenAiChatResponse;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.exception.OpenAiApiException;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.exception.OpenAiErrorCode;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenAiClient {

    private final OpenAiProperties properties;
    private final RestTemplate restTemplate;

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
            ResponseEntity<OpenAiChatResponse> response = restTemplate.exchange(
                properties.getApiUrl(),
                HttpMethod.POST,
                request,
                OpenAiChatResponse.class
            );

            // 응답 파싱
            OpenAiChatResponse chatResponse = response.getBody();
            if (chatResponse == null || chatResponse.getChoices() == null || chatResponse.getChoices().isEmpty()) {
                throw new OpenAiApiException(OpenAiErrorCode.OPENAI_NO_CHOICES);
            }

            return chatResponse.getChoices().get(0).getMessage().getContent();

        } catch (HttpStatusCodeException ex) {
            throw new OpenAiApiException(OpenAiErrorCode.OPENAI_API_ERROR, ex);
        } catch (Exception ex) {
            throw new OpenAiApiException(OpenAiErrorCode.OPENAI_UNEXPECTED_ERROR, ex);
        }
    }
}