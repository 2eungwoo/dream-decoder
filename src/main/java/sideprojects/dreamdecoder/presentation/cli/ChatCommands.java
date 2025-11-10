package sideprojects.dreamdecoder.presentation.cli;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.component.flow.ComponentFlow.ComponentFlowResult;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import sideprojects.dreamdecoder.application.cli.dream.CliChatService;
import sideprojects.dreamdecoder.domain.dream.model.DreamModel;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.global.shared.exception.CustomException;

import java.util.Arrays;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class ChatCommands {
    private final CliChatService cliChatService;

    @ShellMethod(key = "chat", value = "꿈 해몽 요청 (가이드 표시 후, 옵션 채워 재실행)")
    public String chat(
        @ShellOption(defaultValue = ShellOption.NULL) String dreamContent,
        @ShellOption(defaultValue = ShellOption.NULL) String dreamEmotion, // String으로 받음
        @ShellOption(defaultValue = ShellOption.NULL) String aiStyle,      // String으로 받음
        @ShellOption(defaultValue = "") String tags
    ) {
        // 가이드 출력 조건: 하나라도 비어있으면 안내만
        if (dreamContent == null || dreamEmotion == null || aiStyle == null) {
            String emotions = Arrays.stream(DreamEmotion.values()).map(Enum::name).collect(java.util.stream.Collectors.joining(", "));
            String styles   = Arrays.stream(AiStyle.values()).map(Enum::name).collect(java.util.stream.Collectors.joining(", "));
            return """
                   사용법:
                     chat --dreamContent "꿈 내용을 입력" --dreamEmotion EMOTION --aiStyle STYLE [--tags "tag1,tag2"]

                   예시:
                     chat --dreamContent "높은 곳에서 떨어지는 꿈" --dreamEmotion FEAR --aiStyle PROFESSIONAL --tags "공포,추락"

                   허용값:
                     dreamEmotion: %s
                     aiStyle     : %s
                   """.formatted(emotions, styles);
        }

        // (선택) Enum로 검증 — String만 써도 되지만 검증 편의 위해 내부에서 Enum 변환
        DreamEmotion emotionEnum;
        AiStyle styleEnum;
        try {
            emotionEnum = DreamEmotion.valueOf(dreamEmotion.toUpperCase());
            styleEnum   = AiStyle.valueOf(aiStyle.toUpperCase());
        } catch (IllegalArgumentException e) {
            String emotions = Arrays.stream(DreamEmotion.values()).map(Enum::name).collect(java.util.stream.Collectors.joining(", "));
            String styles   = Arrays.stream(AiStyle.values()).map(Enum::name).collect(java.util.stream.Collectors.joining(", "));
            return "허용되지 않은 값입니다.\n- dreamEmotion: " + emotions + "\n- aiStyle: " + styles;
        }

        try {
            cliChatService.checkLoginStatus();
            cliChatService.requestAnalysis(dreamContent, emotionEnum, styleEnum, tags);
            return "해몽 분석 요청이 접수되었습니다.";
        } catch (Exception e) {
            return "오류: " + e.getMessage();
        }
    }

    @ShellMethod(key = "get-dream-analysis", value = "특정 꿈 해몽 결과를 조회합니다.")
    public String getDreamAnalysis(@ShellOption(help = "조회할 꿈의 ID") Long dreamId) {
        try {
            DreamModel dream = cliChatService.getDreamAnalysis(dreamId);

            StringBuilder sb = new StringBuilder();
            sb.append("--- 꿈 해몽 결과 ---\n");
            sb.append("꿈 ID: ").append(dream.getId()).append("\n");
            sb.append("꿈 내용: ").append(dream.getDreamContent()).append("\n");
            sb.append("감정: ").append(dream.getDreamEmotion()).append("\n");
            sb.append("스타일: ").append(dream.getAiStyle()).append("\n");
            sb.append("태그: ").append(dream.getTags()).append("\n");
            sb.append("해몽 결과: ").append(dream.getInterpretationResult()).append("\n");
            sb.append("해몽 시간: ").append(dream.getInterpretedAt()).append("\n");
            sb.append("--------------------\n");

            return sb.toString();
        } catch (CustomException e) {
            return "오류: " + e.getMessage();
        } catch (Exception e) {
            return "예상치 못한 오류가 발생했습니다: " + e.getMessage();
        }
    }

    private static String joinEnumNames(Enum<?>[] values) {
        return Arrays.stream(values).map(Enum::name).collect(Collectors.joining(", "));
    }

    private static String safeGet(ComponentFlowResult result, String key) {
        return (result.getContext().containsKey(key)) ? result.getContext().get(key) : null;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}