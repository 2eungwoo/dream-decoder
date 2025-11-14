package sideprojects.dreamdecoder.application.web.dream.usecase.save;

import sideprojects.dreamdecoder.presentation.web.dream.dto.request.SaveDreamRequest;

public interface SaveDreamUseCase {
    void save(SaveDreamRequest request);
}
