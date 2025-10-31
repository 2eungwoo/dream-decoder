package sideprojects.dreamdecoder.application.feedback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideprojects.dreamdecoder.domain.feedback.persistence.FeedbackLog;
import sideprojects.dreamdecoder.domain.feedback.persistence.FeedbackLogRepository;
import sideprojects.dreamdecoder.presentation.feedback.dto.FeedbackRequest;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackLogRepository feedbackLogRepository;

    @Transactional
    public Long saveFeedback(Long userId, FeedbackRequest request) {
        FeedbackLog feedbackLog = FeedbackLog.builder()
                .userId(userId)
                .isSatisfied(request.isSatisfied())
                .dreamQuery(request.dreamQuery())
                .cachedQuery(request.cachedQuery())
                .similarityScore(request.similarityScore())
                .build();

        feedbackLogRepository.save(feedbackLog);
        return feedbackLog.getId();
    }
}
