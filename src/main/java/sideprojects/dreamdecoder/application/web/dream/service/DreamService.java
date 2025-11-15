package sideprojects.dreamdecoder.application.web.dream.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideprojects.dreamdecoder.domain.dream.model.DreamModel;

import java.util.List;
import sideprojects.dreamdecoder.infrastructure.persistence.dream.DreamRepositoryImpl;

@Service
@RequiredArgsConstructor
public class DreamService {

    private final DreamRepositoryImpl repositoryImpl;

    @Transactional
    public DreamModel saveDream(DreamModel dreamModel) {
        return repositoryImpl.save(dreamModel);
    }

    @Transactional(readOnly = true)
    public List<DreamModel> findAllDreams() {
        return repositoryImpl.findAll();
    }

    @Transactional(readOnly = true)
    public DreamModel findDreamById(Long dreamId) {
        return repositoryImpl.findById(dreamId);
    }
}
