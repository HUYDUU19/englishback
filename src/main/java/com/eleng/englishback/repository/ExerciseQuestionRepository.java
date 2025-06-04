package com.eleng.englishback.repository;
import com.eleng.englishback.domain.ExerciseQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExerciseQuestionRepository extends JpaRepository<ExerciseQuestion, Long> {
    List<ExerciseQuestion> findByExerciseId(Long exerciseId);
    
}

