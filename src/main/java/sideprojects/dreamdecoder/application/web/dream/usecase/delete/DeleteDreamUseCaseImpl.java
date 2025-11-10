package sideprojects.dreamdecoder.application.web.dream.usecase.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.web.dream.service.DreamService;

@Service
@RequiredArgsConstructor
public class DeleteDreamUseCaseImpl implements DeleteDreamUseCase {

    private final DreamService dreamService;

//    @Override
//    public void delete(Long dreamId) {
//        dreamService.deleteDream(dreamId);
//    }
}
