package sideprojects.dreamdecoder.presentation.dream.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sideprojects.dreamdecoder.application.dream.usecase.find.FindAllDreamsUseCase;
import sideprojects.dreamdecoder.application.dream.usecase.find.FindOneDreamUseCase;
import sideprojects.dreamdecoder.application.dream.service.InterpretationCacheService;
import sideprojects.dreamdecoder.application.dream.usecase.save.SaveDreamUseCase;
import sideprojects.dreamdecoder.global.aop.PreventDuplicateRequest;
import sideprojects.dreamdecoder.presentation.dream.dto.request.SaveDreamApiRequest;
import sideprojects.dreamdecoder.domain.dream.model.DreamModel;
import sideprojects.dreamdecoder.global.shared.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import sideprojects.dreamdecoder.presentation.dream.dto.request.SaveDreamRequest;
import sideprojects.dreamdecoder.presentation.dream.dto.response.DreamResponseCode;
import sideprojects.dreamdecoder.presentation.dream.dto.response.FindAllDreamResponse;
import sideprojects.dreamdecoder.presentation.dream.dto.response.FindOneDreamResponse;

@RestController
@RequestMapping("/api/dreams")
@RequiredArgsConstructor
public class DreamController {

    private final SaveDreamUseCase saveDreamUseCase;
    private final FindAllDreamsUseCase findAllDreamsUseCase;
    private final FindOneDreamUseCase findOneDreamUseCase;
    private final InterpretationCacheService interpretationCacheService;
    private final ObjectMapper objectMapper;

    @PostMapping
    @PreventDuplicateRequest(key = "#request.interpretationId")
    public ResponseEntity<ApiResponse<Void>> saveDream(
            @RequestBody @Valid SaveDreamApiRequest request) {

        Map<String, Object> cachedContext = interpretationCacheService.getAndRemoveCachedInterpretation(request.getInterpretationId());

        SaveDreamRequest saveDreamRequest = objectMapper.convertValue(cachedContext.get("request"), SaveDreamRequest.class);

        saveDreamUseCase.save(saveDreamRequest);
        return ApiResponse.success(DreamResponseCode.DREAM_SAVE_SUCCESS);
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<FindAllDreamResponse>>> findAllDream() {
        List<DreamModel> dreamModels = findAllDreamsUseCase.findAll();

        List<FindAllDreamResponse> responseList = dreamModels.stream()
            .map(FindAllDreamResponse::of)
            .toList();

        return ApiResponse.success(DreamResponseCode.DREAM_FOUND_ALL_SUCCESS, responseList);
    }

    @GetMapping("/{dreamId}")
    public ResponseEntity<ApiResponse<FindOneDreamResponse>> findOneDream(
        @PathVariable Long dreamId) {
        DreamModel dreamModel = findOneDreamUseCase.findById(dreamId);
        FindOneDreamResponse response = FindOneDreamResponse.of(dreamModel);

        return ApiResponse.success(DreamResponseCode.DREAM_FOUND_SUCCESS, response);
    }
}
