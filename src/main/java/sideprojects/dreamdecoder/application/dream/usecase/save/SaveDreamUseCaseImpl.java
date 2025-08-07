package sideprojects.dreamdecoder.application.dream.usecase.save;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.dream.service.DreamService;
import sideprojects.dreamdecoder.domain.dream.model.Dream;
import sideprojects.dreamdecoder.presentation.dream.dto.request.SaveDreamRequest;

@Service
@RequiredArgsConstructor
public class SaveDreamUseCaseImpl implements SaveDreamUseCase {

    private final DreamService dreamService;

    @Override
    public Dream save(SaveDreamRequest request) {
        Dream dreamToSave = Dream.createNewDream(
            request.userId(),
            request.dreamContent(),
            request.interpretationResult(),
            request.aiStyle()
        );
        return dreamService.saveDream(dreamToSave);
    }
}
