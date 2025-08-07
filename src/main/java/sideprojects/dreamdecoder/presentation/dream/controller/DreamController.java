package sideprojects.dreamdecoder.presentation.dream.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sideprojects.dreamdecoder.application.dream.usecase.SaveDreamUseCase;
import sideprojects.dreamdecoder.domain.dream.model.Dream;
import sideprojects.dreamdecoder.domain.dream.util.response.DreamResponseCode;
import sideprojects.dreamdecoder.global.shared.response.ApiResponse;
import sideprojects.dreamdecoder.presentation.dream.dto.request.SaveDreamRequest;
import sideprojects.dreamdecoder.presentation.dream.dto.response.DreamResponse;

@RestController
@RequestMapping("/dreams")
@RequiredArgsConstructor
public class DreamController {

    private final SaveDreamUseCase saveDreamUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<DreamResponse>> saveDream(@RequestBody @Valid SaveDreamRequest request) {
        Dream dreamToSave = Dream.createNewDream(
            request.userId(),
            request.dreamContent(),
            request.interpretationResult(),
            request.aiStyle()
        );

        Dream savedDream = saveDreamUseCase.save(dreamToSave);
        DreamResponse response = DreamResponse.of(savedDream);

        return ApiResponse.success(DreamResponseCode.DREAM_SAVE_SUCCESS, response);
    }
}
