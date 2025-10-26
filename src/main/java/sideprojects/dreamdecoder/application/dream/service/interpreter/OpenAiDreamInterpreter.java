package sideprojects.dreamdecoder.application.dream.service.interpreter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.service.InterpretationProvider;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.DreamSymbolExtractor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAiDreamInterpreter implements DreamInterpreter {

    private final DreamSymbolExtractor dreamSymbolExtractor;
    private final InterpretationProvider interpretationProvider;

    @Override
    public InterpretationResult interpret(DreamJobPayload payload) {
        // L1 심볼 캐시 확인은 interpretationProvider 호출 시 자동으로 처리됨
        List<DreamType> extractedTypes = dreamSymbolExtractor.extractSymbols(payload.dreamContent());
        String interpretation = interpretationProvider.generateInterpretation(
                payload.style(),
                payload.dreamEmotion(),
                extractedTypes,
                payload.dreamContent()
        );

        return new InterpretationResult(interpretation, extractedTypes);
    }
}
