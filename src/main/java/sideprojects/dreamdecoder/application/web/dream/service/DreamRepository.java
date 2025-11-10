package sideprojects.dreamdecoder.application.web.dream.service;

import java.util.List;
import sideprojects.dreamdecoder.domain.dream.model.DreamModel;

public interface DreamRepository {
    DreamModel save(DreamModel dreamModel);
    List<DreamModel> findAll();
    DreamModel findById(Long id);
    DreamModel update(DreamModel updatedDreamModel);
    void deleteById(Long dreamId);
}
