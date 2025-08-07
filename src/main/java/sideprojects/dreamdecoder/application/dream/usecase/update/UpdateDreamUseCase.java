package sideprojects.dreamdecoder.application.dream.usecase.update;

import sideprojects.dreamdecoder.domain.dream.model.Dream;

public interface UpdateDreamUseCase {
    Dream update(Long dreamId, Dream updatedDream);
}
