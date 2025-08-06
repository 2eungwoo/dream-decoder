package sideprojects.dreamdecoder.infrastructure.external.openai.service;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class AiStyleService {
    private final ConcurrentHashMap<Long, String> styleCache = new ConcurrentHashMap<>();

    public void setStyle(Long userId, String style) {
        styleCache.put(userId, style);
    }

    public String getStyle(Long userId) {
        return styleCache.getOrDefault(userId, "DEFAULT");
    }
}