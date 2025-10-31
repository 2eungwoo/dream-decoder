package sideprojects.dreamdecoder.domain.feedback.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sideprojects.dreamdecoder.global.shared.persistence.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "feedback_log")
public class FeedbackLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private boolean isSatisfied; // true: 만족(👍), false: 불만족(👎)

    @Lob
    @Column(nullable = false)
    private String dreamQuery; // 사용자가 입력한 원본 꿈 내용

    @Lob
    @Column(nullable = true)
    private String cachedQuery; // 매칭된 캐시의 꿈 내용 (캐시 히트 시)

    @Column(nullable = true)
    private Double similarityScore; // 유사도 점수 (캐시 히트 시)

    @Builder
    public FeedbackLog(Long userId, boolean isSatisfied, String dreamQuery, String cachedQuery,
        Double similarityScore) {
        this.userId = userId;
        this.isSatisfied = isSatisfied;
        this.dreamQuery = dreamQuery;
        this.cachedQuery = cachedQuery;
        this.similarityScore = similarityScore;
    }
}
