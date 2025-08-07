package sideprojects.dreamdecoder.infrastructure.external.openai.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OpenAiChatResponse {
    private List<Choice> choices;

    public OpenAiChatResponse(List<Choice> choices) {
        this.choices = choices;
    }

    @Getter
    @NoArgsConstructor
    public static class Choice {
        private Message message;

        public Choice(Message message) {
            this.message = message;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Message {
        private String content;
        private String role;

        public Message(String content, String role) {
            this.content = content;
            this.role = role;
        }
    }
}
