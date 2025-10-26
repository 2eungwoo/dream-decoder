package sideprojects.dreamdecoder.application.dream.service.interpreter;

public interface DreamInterpreter {
    InterpretationResult interpret(DreamJobPayload payload);
}
