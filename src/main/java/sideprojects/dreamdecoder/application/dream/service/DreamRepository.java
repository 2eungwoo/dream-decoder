package sideprojects.dreamdecoder.application.dream.service;

import java.util.List;
import sideprojects.dreamdecoder.domain.dream.model.Dream;

public interface DreamRepository {
    Dream save(Dream dream);
    List<Dream> findAll();
    Dream findById(Long id);
    Dream update(Dream updatedDream);
    void deleteById(Long dreamId);
}
