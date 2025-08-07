package sideprojects.dreamdecoder.application.dream.usecase;

import sideprojects.dreamdecoder.domain.dream.model.Dream;

import java.util.Optional;

public interface FindDreamByIdUseCase {
    Optional<Dream> findById(Long id);
}
