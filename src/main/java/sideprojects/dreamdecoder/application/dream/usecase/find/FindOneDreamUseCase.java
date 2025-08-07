package sideprojects.dreamdecoder.application.dream.usecase.find;

import sideprojects.dreamdecoder.domain.dream.model.Dream;

public interface FindOneDreamUseCase {
    Dream findById(Long id);
}
