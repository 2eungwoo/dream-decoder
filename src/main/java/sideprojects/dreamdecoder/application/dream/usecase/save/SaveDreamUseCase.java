package sideprojects.dreamdecoder.application.dream.usecase.save;

import sideprojects.dreamdecoder.domain.dream.model.DreamModel;
import sideprojects.dreamdecoder.presentation.dream.dto.request.SaveDreamRequest;

public interface SaveDreamUseCase {
    void save(SaveDreamRequest request);
}
