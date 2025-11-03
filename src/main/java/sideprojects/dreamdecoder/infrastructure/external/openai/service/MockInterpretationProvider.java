package sideprojects.dreamdecoder.infrastructure.external.openai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@Profile("load-test")
@Primary
public class MockInterpretationProvider extends InterpretationProvider {

    // openai 실제 호출 안할거라서
    // OpenAiClient는 null로 주입
    public MockInterpretationProvider() {
        super(null);
    }

    @Override
    public String generateInterpretation(AiStyle style, DreamEmotion dreamEmotion, List<DreamType> extractedTypes, String dreamContent) {
        log.info("[TEST LOG] MockInterpretationProvider 호출, openai 있다고 가정하고 시뮬");
        try {
            // LLM 소요시간 10~20초라고 놓고 테스트
            long sleepTime = ThreadLocalRandom.current().nextLong(10000, 20000);
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "부하 테스트용 가짜 해몽 결과";
    }
}
