package sideprojects.dreamdecoder.application.dream.usecase.save;

import sideprojects.dreamdecoder.domain.dream.model.Dream;
import sideprojects.dreamdecoder.presentation.dream.dto.request.SaveDreamRequest;

public interface SaveDreamUseCase {
    Dream save(SaveDreamRequest request);
}
