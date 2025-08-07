package sideprojects.dreamdecoder.infrastructure.external.openai.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import sideprojects.dreamdecoder.infrastructure.external.openai.dto.response.OpenAiChatResponse;
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
        try {
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(buildRequestBody(systemPrompt, userPrompt), buildHttpHeaders());

            ResponseEntity<OpenAiChatResponse> response = executeChatRequest(request);

            return parseChatResponse(response);

        } catch (HttpStatusCodeException ex) {
            throw new OpenAiApiException(OpenAiErrorCode.OPENAI_API_ERROR, ex);
        } catch (Exception ex) {
            throw new OpenAiApiException(OpenAiErrorCode.OPENAI_UNEXPECTED_ERROR, ex);
        }
    }

    private Map<String, Object> buildRequestBody(String systemPrompt, String userPrompt) {
        return Map.of(
            "model", properties.getModel(),
            "messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userPrompt)
            )
        );
    }

    private HttpHeaders buildHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getApiKey());
        return headers;
    }

    private ResponseEntity<OpenAiChatResponse> executeChatRequest(HttpEntity<Map<String, Object>> request) {
        return restTemplate.exchange(
            properties.getApiUrl(),
            HttpMethod.POST,
            request,
            OpenAiChatResponse.class
        );
    }

    private String parseChatResponse(ResponseEntity<OpenAiChatResponse> response) {
        OpenAiChatResponse chatResponse = response.getBody();
        if (chatResponse == null || chatResponse.choices() == null || chatResponse.choices().isEmpty()) {
            throw new OpenAiApiException(OpenAiErrorCode.OPENAI_NO_CHOICES);
        }
        return chatResponse.choices().get(0).message().content();
    }
}