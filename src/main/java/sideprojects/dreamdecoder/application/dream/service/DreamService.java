package sideprojects.dreamdecoder.application.dream.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideprojects.dreamdecoder.application.dream.validator.DreamValidator;
import sideprojects.dreamdecoder.domain.dream.model.Dream;
import sideprojects.dreamdecoder.domain.dream.persistence.DreamEntity;
import sideprojects.dreamdecoder.domain.dream.util.mapper.DreamMapper;

import java.util.List;
import sideprojects.dreamdecoder.infrastructure.persistence.dream.DreamRepositoryImpl;

@Service
@RequiredArgsConstructor
public class DreamService {

    private final DreamRepositoryImpl repositoryImpl;
    private final DreamValidator dreamValidator;
    private final DreamMapper dreamMapper;

    @Transactional
    public Dream saveDream(Dream dream) {
        return repositoryImpl.save(dream);
    }

    @Transactional(readOnly = true)
    public List<Dream> findAllDreams() {
        return repositoryImpl.findAll();
    }

    @Transactional(readOnly = true)
    public Dream findDreamById(Long dreamId) {
        DreamEntity entity = dreamValidator.validateExistingDreamAndFind(dreamId);
        return dreamMapper.toModel(entity);
    }

//    @Transactional
//    public Dream updateDream(Long dreamId, UpdateDreamRequest request) {
//        dreamValidator.validateExistingDream(dreamId);
//        return repositoryImpl.update(request);
//    }
//
//    @Transactional
//    public void deleteDream(Long dreamId) {
//        dreamValidator.validateExistingDream(dreamId);
//        repositoryImpl.deleteById(dreamId);
//    }
}
