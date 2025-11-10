package sideprojects.dreamdecoder.application.web.dream.usecase.update;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.web.dream.service.DreamService;

@Service
@RequiredArgsConstructor
public class UpdateDreamUseCaseImpl implements UpdateDreamUseCase {

    private final DreamService dreamService;

//    @Override
//    public Dream update(Long dreamId, UpdateDreamRequest request) {
//        return dreamService.updateDream(dreamId, request);
//    }
}
