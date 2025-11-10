package sideprojects.dreamdecoder.application.cli.dream;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.web.dream.service.DreamAnalysisRequestService;
import sideprojects.dreamdecoder.application.web.dream.service.DreamService; // Import DreamService
import sideprojects.dreamdecoder.domain.auth.persistence.User;
import sideprojects.dreamdecoder.domain.auth.persistence.UserRepository;
import sideprojects.dreamdecoder.domain.dream.model.DreamModel; // Import DreamModel
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.infrastructure.security.TokenManager;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.infrastructure.security.jwt.JwtTokenProvider;
import sideprojects.dreamdecoder.presentation.web.dream.dto.request.DreamInterpretationRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CliChatService {

    private final DreamAnalysisRequestService dreamAnalysisRequestService;
    private final DreamService dreamService;
    private final TokenManager tokenManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public void requestAnalysis(String dreamContent, DreamEmotion emotion, AiStyle style, String tags) {
        Long userId = getUserIdFromToken();
        String idempotencyKey = UUID.randomUUID().toString();
        DreamInterpretationRequest request = new DreamInterpretationRequest(dreamContent, emotion, tags, style);
        dreamAnalysisRequestService.requestAnalysis(userId, idempotencyKey, request);
    }

    public DreamModel getDreamAnalysis(Long dreamId) {
        Long userId = getUserIdFromToken();
        DreamModel dream = dreamService.findDreamById(dreamId);

        if (dream == null) {
            throw new RuntimeException("해당 ID의 꿈 해몽을 찾을 수 없습니다.");
        }
        if (!dream.getUserId().equals(userId)) {
            throw new RuntimeException("다른 사용자의 꿈 해몽은 조회할 수 없습니다.");
        }
        return dream;
    }

    public void checkLoginStatus() {
        getUserIdFromToken();
    }

    private Long getUserIdFromToken() {
        String token = tokenManager.getToken()
                .orElseThrow(() -> new RuntimeException("로그인이 필요합니다. 'login' 명령어를 사용해 로그인해주세요."));

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RuntimeException("유효하지 않은 토큰입니다. 다시 로그인해주세요.");
        }

        String username = jwtTokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("토큰에 해당하는 사용자를 찾을 수 없습니다: " + username));
        return user.getId();
    }
}