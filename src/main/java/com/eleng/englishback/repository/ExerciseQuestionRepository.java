package com.eleng.englishback.repository;

import com.eleng.englishback.domain.ExerciseQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseQuestionRepository extends JpaRepository<ExerciseQuestion, Long> {

    List<ExerciseQuestion> findByExerciseId(Long exerciseId);

    List<ExerciseQuestion> findByExerciseIdOrderByIdAsc(Long exerciseId);

    List<ExerciseQuestion> findByType(String type);

    @Query("SELECT eq FROM ExerciseQuestion eq WHERE eq.exercise.id = :exerciseId AND eq.type = :type")
    List<ExerciseQuestion> findByExerciseIdAndType(@Param("exerciseId") Long exerciseId, @Param("type") String type);

    @Query("SELECT COUNT(eq) FROM ExerciseQuestion eq WHERE eq.exercise.id = :exerciseId")
    Long countByExerciseId(@Param("exerciseId") Long exerciseId);

    @Query("SELECT eq FROM ExerciseQuestion eq WHERE eq.exercise.id = :exerciseId AND eq.audioUrl IS NOT NULL")
    List<ExerciseQuestion> findByExerciseIdWithAudio(@Param("exerciseId") Long exerciseId);

    @Query("SELECT eq FROM ExerciseQuestion eq WHERE eq.exercise.id = :exerciseId AND eq.imageUrl IS NOT NULL")
    List<ExerciseQuestion> findByExerciseIdWithImage(@Param("exerciseId") Long exerciseId);
}
