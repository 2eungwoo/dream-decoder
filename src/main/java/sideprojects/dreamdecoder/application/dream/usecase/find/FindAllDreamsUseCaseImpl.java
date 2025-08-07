package sideprojects.dreamdecoder.application.dream.usecase.find;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.dream.service.DreamService;
import sideprojects.dreamdecoder.domain.dream.model.Dream;
import sideprojects.dreamdecoder.application.dream.usecase.find.FindAllDreamsUseCase;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllDreamsUseCaseImpl implements FindAllDreamsUseCase {

    private final DreamService dreamService;

    @Override
    public List<Dream> findAll() {
        return dreamService.findAllDreams();
    }
}
