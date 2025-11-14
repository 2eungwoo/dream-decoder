package sideprojects.dreamdecoder.application.web.dream.service.interpreter;

public interface DreamInterpreter {
    InterpretationResult interpret(DreamJobPayload payload);
}
