
package com.eleng.englishback.domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Entity
@Table(name = "user_usage_limits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUsageLimit {

    // Constants for basic user limits
    public static final int BASIC_MONTHLY_EXERCISE_LIMIT = 50;
    public static final int BASIC_MONTHLY_VOCABULARY_LIMIT = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User ID cannot be null")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Min(value = 0, message = "Daily limit must be at least 0")
    @Column(name = "daily_limit")
    private Integer dailyLimit;

    @Min(value = 0, message = "Monthly limit must be at least 0")
    @Column(name = "monthly_limit")
    private Integer monthlyLimit;

    @Min(value = 0, message = "Current daily usage must be at least 0")
    @Column(name = "current_daily_usage")
    private Integer currentDailyUsage;

    @Min(value = 0, message = "Current monthly usage must be at least 0")
    @Column(name = "current_monthly_usage")
    private Integer currentMonthlyUsage;

    @Column(name = "last_reset_date")
    private LocalDateTime lastResetDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (currentDailyUsage == null)
            currentDailyUsage = 0;
        if (currentMonthlyUsage == null)
            currentMonthlyUsage = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Helper methods for PremiumController
    public int getMonthlyExercisesUsed() {
        return currentMonthlyUsage != null ? currentMonthlyUsage : 0;
    }

    public int getRemainingExercises() {
        return Math.max(0, BASIC_MONTHLY_EXERCISE_LIMIT - getMonthlyExercisesUsed());
    }

    public int getMonthlyVocabularyCardsUsed() {
        // For now, we'll use the same counter, but this could be separate
        return currentMonthlyUsage != null ? currentMonthlyUsage : 0;
    }

    public int getRemainingVocabularyCards() {
        return Math.max(0, BASIC_MONTHLY_VOCABULARY_LIMIT - getMonthlyVocabularyCardsUsed());
    }

    public boolean hasReachedExerciseLimit() {
        return getMonthlyExercisesUsed() >= BASIC_MONTHLY_EXERCISE_LIMIT;
    }

    public boolean hasReachedVocabularyLimit() {
        return getMonthlyVocabularyCardsUsed() >= BASIC_MONTHLY_VOCABULARY_LIMIT;
    }
    public boolean isLimitExceeded() {
        return hasReachedExerciseLimit() || hasReachedVocabularyLimit();
    }
    //create getUserUsageLimitsByUserId method 
    public static UserUsageLimit createDefaultLimit(Long userId) {
        return UserUsageLimit.builder()
                .userId(userId)
                .dailyLimit(100) // Default daily limit
                .monthlyLimit(2000) // Default monthly limit
                .currentDailyUsage(0)
                .currentMonthlyUsage(0)
                .lastResetDate(LocalDateTime.now())
                .build();
    }
    
}
