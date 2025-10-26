package sideprojects.dreamdecoder.application.dream.service.interpreter;

import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;

import java.util.List;

public record InterpretationResult(String interpretation, List<DreamType> dreamTypes) {
}
