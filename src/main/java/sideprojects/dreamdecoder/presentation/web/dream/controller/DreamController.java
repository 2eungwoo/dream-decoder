package sideprojects.dreamdecoder.presentation.web.dream.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sideprojects.dreamdecoder.application.web.dream.usecase.find.FindAllDreamsUseCase;
import sideprojects.dreamdecoder.application.web.dream.usecase.find.FindOneDreamUseCase;
import sideprojects.dreamdecoder.domain.dream.model.DreamModel;
import sideprojects.dreamdecoder.global.shared.response.ApiResponse;
import sideprojects.dreamdecoder.presentation.web.dream.dto.response.DreamInterpretationResponse;
import sideprojects.dreamdecoder.presentation.web.dream.dto.response.DreamResponseCode;
import sideprojects.dreamdecoder.presentation.web.dream.dto.response.FindAllDreamResponse;

@RestController
@RequestMapping("/api/dreams")
@RequiredArgsConstructor
public class DreamController {

    private final FindAllDreamsUseCase findAllDreamsUseCase;
    private final FindOneDreamUseCase findOneDreamUseCase;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<FindAllDreamResponse>>> findAllDream() {
        List<DreamModel> dreamModels = findAllDreamsUseCase.findAll();

        List<FindAllDreamResponse> responseList = dreamModels.stream()
            .map(FindAllDreamResponse::of)
            .toList();

        return ApiResponse.success(DreamResponseCode.DREAM_FOUND_ALL_SUCCESS, responseList);
    }

    @GetMapping("/{dreamId}")
    public ResponseEntity<ApiResponse<DreamInterpretationResponse>> findOneDream(
        @PathVariable Long dreamId) {
        DreamModel dreamModel = findOneDreamUseCase.findById(dreamId);
        DreamInterpretationResponse response = DreamInterpretationResponse.of(dreamModel);

        return ApiResponse.success(DreamResponseCode.DREAM_FOUND_SUCCESS, response);
    }
}
