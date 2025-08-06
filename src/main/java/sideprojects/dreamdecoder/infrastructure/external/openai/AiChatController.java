package sideprojects.dreamdecoder.infrastructure.external.openai;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sideprojects.dreamdecoder.infrastructure.external.openai.service.OpenAiChatService;


@RestController
@RequiredArgsConstructor
public class AiChatController {
    private final OpenAiChatService openAiChatService;

    @PostMapping("/ai/chat")
    public ResponseEntity<String> chat(@RequestParam Long userId, @RequestBody String prompt) {
        String result = openAiChatService.chat(userId, prompt);
        return ResponseEntity.ok(result);
    }
}
