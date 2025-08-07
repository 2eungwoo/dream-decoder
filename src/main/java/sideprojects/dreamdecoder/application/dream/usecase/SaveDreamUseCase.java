package sideprojects.dreamdecoder.application.dream.usecase;

import sideprojects.dreamdecoder.domain.dream.model.Dream;

public interface SaveDreamUseCase {
    Dream save(Dream dream);
}
