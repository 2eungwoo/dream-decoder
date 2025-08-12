package sideprojects.dreamdecoder.application.dream.usecase.find;

import sideprojects.dreamdecoder.domain.dream.model.DreamModel;

import java.util.List;

public interface FindAllDreamsUseCase {
    List<DreamModel> findAll();
}
