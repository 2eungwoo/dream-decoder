package sideprojects.dreamdecoder.application.dream.usecase.find;

import sideprojects.dreamdecoder.domain.dream.model.DreamModel;

public interface FindOneDreamUseCase {
    DreamModel findById(Long id);
}
