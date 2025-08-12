package sideprojects.dreamdecoder.presentation.dream.controller;

import jakarta.validation.Valid;
import java.util.List;
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
import sideprojects.dreamdecoder.application.dream.usecase.save.SaveDreamUseCase;
import sideprojects.dreamdecoder.domain.dream.model.Dream;
import sideprojects.dreamdecoder.global.shared.response.ApiResponse;
import sideprojects.dreamdecoder.presentation.dream.dto.request.SaveDreamRequest;
import sideprojects.dreamdecoder.presentation.dream.dto.response.DreamResponseCode;
import sideprojects.dreamdecoder.presentation.dream.dto.response.FindAllDreamResponse;
import sideprojects.dreamdecoder.presentation.dream.dto.response.FindOneDreamResponse;
import sideprojects.dreamdecoder.presentation.dream.dto.response.SaveDreamResponse;

@RestController
@RequestMapping("/api/dreams")
@RequiredArgsConstructor
public class DreamController {

    private final SaveDreamUseCase saveDreamUseCase;
    private final FindAllDreamsUseCase findAllDreamsUseCase;
    private final FindOneDreamUseCase findOneDreamUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<SaveDreamResponse>> saveDream(
        @RequestBody @Valid SaveDreamRequest request) {
        Dream savedDream = saveDreamUseCase.save(request);
        SaveDreamResponse response = SaveDreamResponse.of(savedDream);

        return ApiResponse.success(DreamResponseCode.DREAM_SAVE_SUCCESS, response);
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<FindAllDreamResponse>>> findAllDream() {
        List<Dream> dreams = findAllDreamsUseCase.findAll();

        List<FindAllDreamResponse> responseList = dreams.stream()
            .map(FindAllDreamResponse::of)
            .toList();

        return ApiResponse.success(DreamResponseCode.DREAM_FOUND_ALL_SUCCESS, responseList);
    }

    @GetMapping("/{dreamId}")
    public ResponseEntity<ApiResponse<FindOneDreamResponse>> findOneDream(
        @PathVariable Long dreamId) {
        Dream dream = findOneDreamUseCase.findById(dreamId);
        FindOneDreamResponse response = FindOneDreamResponse.of(dream);

        return ApiResponse.success(DreamResponseCode.DREAM_FOUND_SUCCESS, response);
    }
}
