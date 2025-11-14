package sideprojects.dreamdecoder.application.web.dream.usecase.find;

import sideprojects.dreamdecoder.domain.dream.model.DreamModel;

public interface FindOneDreamUseCase {
    DreamModel findById(Long id);
}
