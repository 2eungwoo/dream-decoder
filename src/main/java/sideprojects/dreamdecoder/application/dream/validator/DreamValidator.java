package sideprojects.dreamdecoder.application.dream.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.domain.dream.persistence.DreamEntity;
import sideprojects.dreamdecoder.domain.dream.util.exception.DreamNotFoundException;
import sideprojects.dreamdecoder.infrastructure.persistence.dream.DreamJpaRepository;

@Component
@RequiredArgsConstructor
public class DreamValidator {

    private final DreamJpaRepository repository;

    public void validateExistingDream(Long dreamId) {
        repository.findById(dreamId)
            .orElseThrow(DreamNotFoundException::new);
    }

    public DreamEntity validateExistingDreamAndFind(Long dreamId) {
        return repository.findById(dreamId)
            .orElseThrow(DreamNotFoundException::new);
    }

}