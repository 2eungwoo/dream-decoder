package sideprojects.dreamdecoder.domain.feedback.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackLogRepository extends JpaRepository<FeedbackLog, Long> {
}
