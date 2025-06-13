package com.eleng.englishback.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_flashcard_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFlashcardProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "flashcard_card_id", nullable = false)
    private Long flashcardCardId;

    @Enumerated(EnumType.STRING)
    @Column(name = "mastery_level", nullable = false)
    private MasteryLevel masteryLevel = MasteryLevel.LEARNING;

    @Column(name = "correct_attempts")
    private Integer correctAttempts = 0;

    @Column(name = "total_attempts")
    private Integer totalAttempts = 0;

    @Column(name = "last_reviewed_at")
    private LocalDateTime lastReviewedAt;

    @Column(name = "next_review_at")
    private LocalDateTime nextReviewAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // Many-to-one relationships
    // Temporarily commented out due to User class compilation issues
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id", insertable = false, updatable = false)
    // private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flashcard_card_id", insertable = false, updatable = false)
    private FlashcardCard flashcardCard;

    public enum MasteryLevel {
        LEARNING,
        REVIEWING,
        CONFIDENT,
        MASTERED
    }
}
