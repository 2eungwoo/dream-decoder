package sideprojects.dreamdecoder.application.dream.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.application.dream.usecase.save.SaveDreamUseCase;
import sideprojects.dreamdecoder.presentation.dream.dto.request.SaveDreamRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class DreamEventListener {

    private final SaveDreamUseCase saveDreamUseCase;

    @Async
    @EventListener
    public void handleDreamInterpretationCompletedEvent(DreamInterpretationCompletedEvent event) {
        log.info("비동기 이벤트 수신: 꿈 해석 결과 DB 저장 (유저 ID: {})", event.userId());
        try {
            SaveDreamRequest request = SaveDreamRequest.builder()
                    .userId(event.userId())
                    .dreamContent(event.dreamContent())
                    .interpretationResult(event.interpretationResult())
                    .aiStyle(event.aiStyle())
                    .dreamTypes(event.dreamTypes())
                    .build();
            saveDreamUseCase.save(request);
            log.info("꿈 해석 결과 DB 저장 성공 (유저 ID: {})", event.userId());
        } catch (Exception e) {
            log.error("꿈 해석 결과 DB 저장 중 오류 발생 (유저 ID: {})", event.userId(), e);
            // TODO: 재시도 로직 또는 실패 알림 등 추가 구현 가능
        }
    }
}
