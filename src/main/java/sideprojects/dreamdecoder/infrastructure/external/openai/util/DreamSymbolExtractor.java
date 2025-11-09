package sideprojects.dreamdecoder.infrastructure.external.openai.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.config.OpenAiClient;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.exception.OpenAiApiException;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.exception.OpenAiErrorCode;

import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.cache.annotation.Cacheable;

@Slf4j
@Service
@RequiredArgsConstructor
public class DreamSymbolExtractor {

    private final OpenAiClient openAiClient;
    private final ObjectMapper objectMapper;

    @Cacheable(cacheManager = "localCacheManager", cacheNames = "dreamSymbols", keyGenerator = "dreamSymbolsKeyGenerator")
    public List<DreamType> extractSymbols(AiStyle style, DreamEmotion dreamEmotion, String dreamContent) {
        List<String> allTypeNames = Stream.of(DreamType.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        String systemPrompt = PromptGenerator.generateSymbolExtractionSystemPrompt(allTypeNames);
        String jsonResponse = openAiClient.chat(systemPrompt, dreamContent);
        log.info("AI가 추출한 SYMBOLE JSON 응답: {}", jsonResponse);

        try {
            List<String> extractedKeywords = objectMapper.readValue(jsonResponse, new TypeReference<>() {});
            return extractedKeywords.stream()
                    .filter(keyword -> {
                        try {
                            DreamType.valueOf(keyword);
                            return true;
                        } catch (IllegalArgumentException e) {
                            log.warn("AI가 인식한 키워드 '{}'는 DreamType enum에 존재하지 않습니다. 무시합니다.", keyword);
                            return false;
                        }
                    })
                    .map(DreamType::valueOf)
                    .collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            log.error("ai 응답 json 파싱 에러: ",e);
            throw new OpenAiApiException(OpenAiErrorCode.OPENAI_JSON_PARSING_ERROR, e);
        }
    }
}
