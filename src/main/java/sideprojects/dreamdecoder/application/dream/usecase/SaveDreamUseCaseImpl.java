package sideprojects.dreamdecoder.application.dream.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.dream.service.DreamService;
import sideprojects.dreamdecoder.domain.dream.model.Dream;

@Service
@RequiredArgsConstructor
public class SaveDreamUseCaseImpl implements SaveDreamUseCase {

    private final DreamService dreamService;

    @Override
    public Dream save(Dream dream) {
        return dreamService.saveDream(dream);
    }
}
