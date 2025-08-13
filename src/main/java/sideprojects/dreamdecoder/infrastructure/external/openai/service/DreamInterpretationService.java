package sideprojects.dreamdecoder.infrastructure.external.openai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.dream.usecase.save.SaveDreamUseCase;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.presentation.dream.dto.request.SaveDreamRequest;
import sideprojects.dreamdecoder.presentation.dream.dto.response.DreamInterpretationResponse;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DreamInterpretationService {

    
    private final DreamSymbolExtractorService dreamSymbolExtractorService;
    private final DreamInterpretationGeneratorService dreamInterpretationGeneratorService;
    private final SaveDreamUseCase saveDreamUseCase;

    public DreamInterpretationResponse interpretDream(Long userId, String dreamContent, AiStyle style) {
        AiStyle actualStyle = AiStyle.from(style);

        // 사용자 채팅에서 키워드 추출
        List<DreamType> extractedTypes = dreamSymbolExtractorService.extractSymbols(dreamContent);

        // 추출 키워드로 해몽 생성
        String interpretation = dreamInterpretationGeneratorService.generateInterpretation(actualStyle, extractedTypes, dreamContent);

        SaveDreamRequest dreamSaveRequest = new SaveDreamRequest(userId, dreamContent, interpretation, actualStyle, extractedTypes);
        saveDreamUseCase.save(dreamSaveRequest);

        return DreamInterpretationResponse.of(interpretation, actualStyle, extractedTypes);
    }
}

