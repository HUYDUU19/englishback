package com.eleng.englishback.repository;

import com.eleng.englishback.domain.UserUsageLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserUsageLimitRepository extends JpaRepository<UserUsageLimit, Long> {
    
    Optional<UserUsageLimit> findByUserId(Long userId);
    
    @Query("SELECT uul FROM UserUsageLimit uul WHERE uul.lastResetDate < ?1")
    List<UserUsageLimit> findUsageNeedingReset(LocalDate beforeDate);
    
    @Query("SELECT COUNT(uul) FROM UserUsageLimit uul WHERE uul.monthlyExercisesUsed >= ?1")
    Long countUsersAtExerciseLimit(Integer limit);
    
    @Query("SELECT COUNT(uul) FROM UserUsageLimit uul WHERE uul.monthlyVocabularyCardsUsed >= ?1")
    Long countUsersAtVocabularyLimit(Integer limit);

    
}
