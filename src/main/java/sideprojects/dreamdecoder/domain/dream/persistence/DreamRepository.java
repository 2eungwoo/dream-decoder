package sideprojects.dreamdecoder.domain.dream.persistence;

import sideprojects.dreamdecoder.domain.dream.model.Dream;

public interface DreamRepository {
    Dream save(Dream dream);
}
