package sideprojects.dreamdecoder.domain.dream.persistence;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Table(name = "dreams")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DreamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String dreamContent;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String interpretationResult;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AiStyle aiStyle;

    @ElementCollection
    @CollectionTable(name = "dream_symbols", joinColumns = @JoinColumn(name = "dream_id"))
    private List<DreamSymbol> dreamSymbols;

    @Column(name = "interpreted_at", nullable = false)
    private LocalDateTime interpretedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Builder
    private DreamEntity(Long id, Long userId,
        String dreamContent, String interpretationResult,
        AiStyle aiStyle, List<DreamSymbol> dreamSymbols,
        LocalDateTime interpretedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.id = id;
        this.userId = userId;
        this.dreamContent = dreamContent;
        this.interpretationResult = interpretationResult;
        this.aiStyle = aiStyle;
        this.dreamSymbols = dreamSymbols;
        this.interpretedAt = interpretedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
