package com.eleng.englishback.repository;

import com.eleng.englishback.domain.UserQuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserQuestionAnswerRepository extends JpaRepository<UserQuestionAnswer, Long> {
    List<UserQuestionAnswer> findByAttemptId(Long attemptId);

    List<UserQuestionAnswer> findByQuestionId(Long questionId);

    Optional<UserQuestionAnswer> findByAttemptIdAndQuestionId(Long attemptId, Long questionId);

    @Query("SELECT uqa FROM UserQuestionAnswer uqa WHERE uqa.attemptId = :attemptId ORDER BY uqa.questionId ASC")
    List<UserQuestionAnswer> findByAttemptIdOrderByQuestionId(@Param("attemptId") Long attemptId);

    @Query("SELECT COUNT(uqa) FROM UserQuestionAnswer uqa WHERE uqa.attemptId = :attemptId AND uqa.isCorrect = true")
    Long countCorrectAnswersByAttemptId(@Param("attemptId") Long attemptId);

    @Query("SELECT COUNT(uqa) FROM UserQuestionAnswer uqa WHERE uqa.attemptId = :attemptId")
    Long countTotalAnswersByAttemptId(@Param("attemptId") Long attemptId);

    @Query("SELECT uqa FROM UserQuestionAnswer uqa WHERE uqa.attempt.user.id = :userId AND uqa.questionId = :questionId")
    List<UserQuestionAnswer> findByUserIdAndQuestionId(@Param("userId") Long userId,
            @Param("questionId") Long questionId);

    @Query("SELECT uqa FROM UserQuestionAnswer uqa WHERE uqa.attemptId = :attemptId AND uqa.isCorrect = :isCorrect")
    List<UserQuestionAnswer> findByAttemptIdAndIsCorrect(@Param("attemptId") Long attemptId,
            @Param("isCorrect") boolean isCorrect);

    @Query("SELECT AVG(CASE WHEN uqa.isCorrect = true THEN 1.0 ELSE 0.0 END) FROM UserQuestionAnswer uqa WHERE uqa.attempt.user.id = :userId")
    Double getAverageAccuracyByUserId(@Param("userId") Long userId);

    @Query("SELECT uqa FROM UserQuestionAnswer uqa WHERE uqa.attempt.user.id = :userId AND uqa.question.type = :questionType")
    List<UserQuestionAnswer> findByUserIdAndQuestionType(@Param("userId") Long userId,
            @Param("questionType") String questionType);
}
