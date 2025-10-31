package sideprojects.dreamdecoder.application.feedback.service

import sideprojects.dreamdecoder.domain.feedback.persistence.FeedbackLog
import sideprojects.dreamdecoder.domain.feedback.persistence.FeedbackLogRepository
import sideprojects.dreamdecoder.presentation.feedback.dto.FeedbackRequest
import spock.lang.Specification

class FeedbackServiceSpec extends Specification {

    def feedbackLogRepository = Mock(FeedbackLogRepository)
    def feedbackService = new FeedbackService(feedbackLogRepository)

    def "피드백 생성 테스트"() {
        given: "피드백 저장 요청 데이터로"
        def userId = 1L
        def request = new FeedbackRequest(true, "좋은 꿈이었어요", "용이 나오는 꿈", 0.8)

        when: "저장로직 호출하면"
        feedbackService.saveFeedback(userId, request)

        then: "repository.save() 호출됨"
        1 * feedbackLogRepository.save(_ as FeedbackLog) >> { FeedbackLog log ->
            assert log.getUserId() == userId
            assert log.isSatisfied() == request.isSatisfied()
            assert log.getDreamQuery() == request.dreamQuery()
            assert log.getCachedQuery() == request.cachedQuery()
            assert log.getSimilarityScore() == request.similarityScore()
        }
    }
}
