package sideprojects.dreamdecoder.application.web.dream.usecase.save;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.web.dream.producer.DreamSaveJobCommand;
import sideprojects.dreamdecoder.application.web.dream.producer.DreamSaveJobProducer;
import sideprojects.dreamdecoder.presentation.web.dream.dto.request.SaveDreamRequest;

@Service
@RequiredArgsConstructor
public class SaveDreamUseCaseImpl implements SaveDreamUseCase {

    private final DreamSaveJobProducer dreamSaveJobProducer;

    @Override
    public void save(SaveDreamRequest request) {
        DreamSaveJobCommand command = DreamSaveJobCommand.builder()
            .userId(request.userId())
            .dreamContent(request.dreamContent())
            .interpretation(request.interpretationResult())
            .dreamEmotion(request.dreamEmotion())
            .tags(request.tags())
            .style(request.aiStyle())
            .types(request.dreamTypes())
            .build();

        dreamSaveJobProducer.publishJob(command);
    }
}
