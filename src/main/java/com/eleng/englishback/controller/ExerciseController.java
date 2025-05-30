package com.eleng.englishback.controller;

import com.eleng.englishback.domain.Exercise;
import com.eleng.englishback.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import com.eleng.englishback.domain.ExerciseQuestion;
import com.eleng.englishback.repository.ExerciseQuestionRepository;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseQuestionRepository exerciseQuestionRepository;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/lesson/{lessonId}")
    public List<Exercise> getByLesson(@PathVariable Long lessonId) {
        return exerciseRepository.findByLessonId(lessonId);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{exerciseId}/questions")
    public List<ExerciseQuestion> getQuestionsByExercise(@PathVariable Long exerciseId) {
        return exerciseQuestionRepository.findByExerciseId(exerciseId);
    }

}
