package sideprojects.dreamdecoder.infrastructure.external.openai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.web.dream.producer.DreamSaveJobCommand;
import sideprojects.dreamdecoder.application.web.dream.producer.DreamSaveJobProducer;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.global.aop.UseSemaphore;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

@Slf4j
@Service
@RequiredArgsConstructor
public class DreamInterpretationService {

    private final DreamSaveJobProducer dreamSaveJobProducer;

    @UseSemaphore
    public void interpretDream(Long userId, String dreamContent,
        DreamEmotion dreamEmotion, String tags, AiStyle style) {

        AiStyle actualStyle = AiStyle.from(style);

        // 해석 요청 작업을 Redis Stream에 발행
        DreamSaveJobCommand command = DreamSaveJobCommand.builder()
            .userId(userId)
            .dreamContent(dreamContent)
            .dreamEmotion(dreamEmotion)
            .tags(tags)
            .style(actualStyle)
            .build();
        dreamSaveJobProducer.publishJob(command);
    }
}
