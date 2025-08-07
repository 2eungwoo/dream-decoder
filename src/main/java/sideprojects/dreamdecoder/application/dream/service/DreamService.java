package sideprojects.dreamdecoder.application.dream.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideprojects.dreamdecoder.domain.dream.model.Dream;
import sideprojects.dreamdecoder.domain.dream.persistence.DreamRepository;

@Service
@RequiredArgsConstructor
public class DreamService {

    private final DreamRepository dreamRepository;

    @Transactional
    public Dream saveDream(Dream dream) {
        return dreamRepository.save(dream);
    }
}
