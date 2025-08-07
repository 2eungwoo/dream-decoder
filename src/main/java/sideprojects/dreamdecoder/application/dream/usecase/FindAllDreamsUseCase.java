package sideprojects.dreamdecoder.application.dream.usecase;

import sideprojects.dreamdecoder.domain.dream.model.Dream;

import java.util.List;

public interface FindAllDreamsUseCase {
    List<Dream> findAll();
}
