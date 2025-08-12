package sideprojects.dreamdecoder.application.dream.usecase.find;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.dream.service.DreamService;
import sideprojects.dreamdecoder.domain.dream.model.DreamModel;

@Service
@RequiredArgsConstructor
public class FindOneDreamUseCaseImpl implements FindOneDreamUseCase {

    private final DreamService dreamService;

    @Override
    public DreamModel findById(Long id) {
        return dreamService.findDreamById(id);
    }
}
