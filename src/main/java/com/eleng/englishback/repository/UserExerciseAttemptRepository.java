package com.eleng.englishback.repository;

import com.eleng.englishback.domain.UserExerciseAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserExerciseAttemptRepository extends JpaRepository<UserExerciseAttempt, Long> {

    List<UserExerciseAttempt> findByUserId(Long userId);

    List<UserExerciseAttempt> findByExerciseId(Long exerciseId);

    List<UserExerciseAttempt> findByUserIdAndExerciseId(Long userId, Long exerciseId);

    @Query("SELECT uea FROM UserExerciseAttempt uea WHERE uea.user.id = :userId AND uea.exercise.id = :exerciseId ORDER BY uea.startedAt DESC")
    List<UserExerciseAttempt> findByUserIdAndExerciseIdOrderByAttemptedAtDesc(@Param("userId") Long userId,
            @Param("exerciseId") Long exerciseId);

    @Query("SELECT uea FROM UserExerciseAttempt uea WHERE uea.user.id = :userId ORDER BY uea.startedAt DESC")
    List<UserExerciseAttempt> findByUserIdOrderByAttemptedAtDesc(@Param("userId") Long userId);

    @Query("SELECT MAX(uea.score) FROM UserExerciseAttempt uea WHERE uea.user.id = :userId AND uea.exercise.id = :exerciseId")
    Optional<Integer> findBestScoreByUserIdAndExerciseId(@Param("userId") Long userId,
            @Param("exerciseId") Long exerciseId);

    @Query("SELECT AVG(uea.score) FROM UserExerciseAttempt uea WHERE uea.user.id = :userId AND uea.exercise.id = :exerciseId")
    Optional<Double> findAverageScoreByUserIdAndExerciseId(@Param("userId") Long userId,
            @Param("exerciseId") Long exerciseId);

    @Query("SELECT COUNT(uea) FROM UserExerciseAttempt uea WHERE uea.user.id = :userId AND uea.exercise.id = :exerciseId")
    Long countAttemptsByUserIdAndExerciseId(@Param("userId") Long userId, @Param("exerciseId") Long exerciseId);

    @Query("SELECT uea FROM UserExerciseAttempt uea WHERE uea.user.id = :userId AND uea.score >= :minScore")
    List<UserExerciseAttempt> findByUserIdAndScoreGreaterThanEqual(@Param("userId") Long userId,
            @Param("minScore") Integer minScore);

    @Query("SELECT uea FROM UserExerciseAttempt uea WHERE uea.user.id = :userId AND uea.exercise.type = :exerciseType")
    List<UserExerciseAttempt> findByUserIdAndExerciseType(@Param("userId") Long userId,
            @Param("exerciseType") String exerciseType);

    @Query("SELECT uea FROM UserExerciseAttempt uea WHERE uea.user.id = :userId AND uea.startedAt BETWEEN :startDate AND :endDate")
    List<UserExerciseAttempt> findByUserIdAndDateRange(@Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
