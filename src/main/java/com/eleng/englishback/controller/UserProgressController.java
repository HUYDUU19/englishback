package com.eleng.englishback.controller;

import com.eleng.englishback.domain.UserLessonProgress;
import com.eleng.englishback.domain.UserExerciseAttempt;
import com.eleng.englishback.domain.ProgressStatus;
import com.eleng.englishback.repository.UserLessonProgressRepository;
import com.eleng.englishback.repository.UserExerciseAttemptRepository;
import com.eleng.englishback.repository.UserQuestionAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/progress")
@CrossOrigin(origins = "*")
public class UserProgressController {

    @Autowired
    private UserLessonProgressRepository userLessonProgressRepository;

    @Autowired
    private UserExerciseAttemptRepository userExerciseAttemptRepository;

    @Autowired
    private UserQuestionAnswerRepository userQuestionAnswerRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserProgress(@PathVariable Long userId) {
        Map<String, Object> progressData = new HashMap<>();

        // Get lesson progress
        List<UserLessonProgress> lessonProgress = userLessonProgressRepository.findByUserIdOrderByUpdatedAtDesc(userId);

        // Get exercise attempts
        List<UserExerciseAttempt> exerciseAttempts = userExerciseAttemptRepository
                .findByUserIdOrderByAttemptedAtDesc(userId);

        // Calculate statistics
        Long completedLessons = userLessonProgressRepository.countByUserIdAndStatus(userId, ProgressStatus.COMPLETED);
        Long inProgressLessons = userLessonProgressRepository.countByUserIdAndStatus(userId,
                ProgressStatus.IN_PROGRESS);
        Double averageProgress = userLessonProgressRepository.getAverageProgressByUserId(userId);
        Double averageAccuracy = userQuestionAnswerRepository.getAverageAccuracyByUserId(userId);

        progressData.put("lessonProgress", lessonProgress);
        progressData.put("exerciseAttempts", exerciseAttempts);
        progressData.put("completedLessons", completedLessons);
        progressData.put("inProgressLessons", inProgressLessons);
        progressData.put("averageProgress", averageProgress != null ? averageProgress : 0.0);
        progressData.put("averageAccuracy", averageAccuracy != null ? averageAccuracy * 100 : 0.0);

        return ResponseEntity.ok(progressData);
    }

    @GetMapping("/user/{userId}/lessons")
    public ResponseEntity<List<UserLessonProgress>> getUserLessonProgress(@PathVariable Long userId) {
        List<UserLessonProgress> progress = userLessonProgressRepository.findByUserIdOrderByUpdatedAtDesc(userId);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/user/{userId}/lessons/status/{status}")
    public ResponseEntity<List<UserLessonProgress>> getUserLessonProgressByStatus(
            @PathVariable Long userId,
            @PathVariable ProgressStatus status) {
        List<UserLessonProgress> progress = userLessonProgressRepository
                .findByUserIdAndStatusOrderByUpdatedAtDesc(userId, status);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/user/{userId}/exercises")
    public ResponseEntity<List<UserExerciseAttempt>> getUserExerciseAttempts(@PathVariable Long userId) {
        List<UserExerciseAttempt> attempts = userExerciseAttemptRepository.findByUserIdOrderByAttemptedAtDesc(userId);
        return ResponseEntity.ok(attempts);
    }

    @GetMapping("/user/{userId}/exercises/type/{type}")
    public ResponseEntity<List<UserExerciseAttempt>> getUserExerciseAttemptsByType(
            @PathVariable Long userId,
            @PathVariable String type) {
        List<UserExerciseAttempt> attempts = userExerciseAttemptRepository.findByUserIdAndExerciseType(userId, type);
        return ResponseEntity.ok(attempts);
    }

    @GetMapping("/user/{userId}/level/{level}")
    public ResponseEntity<List<UserLessonProgress>> getUserProgressByLevel(
            @PathVariable Long userId,
            @PathVariable String level) {
        List<UserLessonProgress> progress = userLessonProgressRepository.findByUserIdAndLessonLevel(userId, level);
        return ResponseEntity.ok(progress);
    }

    @PostMapping("/lesson/{lessonId}/user/{userId}")
    public ResponseEntity<UserLessonProgress> updateLessonProgress(
            @PathVariable Long lessonId,
            @PathVariable Long userId,
            @RequestBody Map<String, Object> progressData) {
        Integer completionPercentage = (Integer) progressData.get("completionPercentage");
        String statusStr = (String) progressData.get("status");
        ProgressStatus status = ProgressStatus.valueOf(statusStr);

        UserLessonProgress progress = userLessonProgressRepository
                .findByUserIdAndLessonId(userId, lessonId)
                .orElse(new UserLessonProgress());

        progress.setUserId(userId);
        progress.setLessonId(lessonId);
        progress.setCompletionPercentage(completionPercentage.doubleValue());
        progress.setStatus(status);

        UserLessonProgress savedProgress = userLessonProgressRepository.save(progress);
        return ResponseEntity.ok(savedProgress);
    }

    @GetMapping("/lesson/{lessonId}/user/{userId}")
    public ResponseEntity<UserLessonProgress> getLessonProgress(
            @PathVariable Long lessonId,
            @PathVariable Long userId) {
        return userLessonProgressRepository.findByUserIdAndLessonId(userId, lessonId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stats/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserStats(@PathVariable Long userId) {
        Map<String, Object> stats = new HashMap<>();

        // Lesson statistics
        Long totalLessons = userLessonProgressRepository.countByUserIdAndStatus(userId, ProgressStatus.NOT_STARTED) +
                userLessonProgressRepository.countByUserIdAndStatus(userId, ProgressStatus.IN_PROGRESS) +
                userLessonProgressRepository.countByUserIdAndStatus(userId, ProgressStatus.COMPLETED);

        Long completedLessons = userLessonProgressRepository.countByUserIdAndStatus(userId, ProgressStatus.COMPLETED);
        Long inProgressLessons = userLessonProgressRepository.countByUserIdAndStatus(userId,
                ProgressStatus.IN_PROGRESS);

        // Exercise statistics
        List<UserExerciseAttempt> allAttempts = userExerciseAttemptRepository.findByUserId(userId);
        Long totalExerciseAttempts = (long) allAttempts.size();
        Double averageScore = allAttempts.stream()
                .mapToDouble(UserExerciseAttempt::getScore)
                .average()
                .orElse(0.0);

        Double averageAccuracy = userQuestionAnswerRepository.getAverageAccuracyByUserId(userId);
        Double averageLessonProgress = userLessonProgressRepository.getAverageProgressByUserId(userId);

        stats.put("totalLessons", totalLessons);
        stats.put("completedLessons", completedLessons);
        stats.put("inProgressLessons", inProgressLessons);
        stats.put("totalExerciseAttempts", totalExerciseAttempts);
        stats.put("averageScore", averageScore);
        stats.put("averageAccuracy", averageAccuracy != null ? averageAccuracy * 100 : 0.0);
        stats.put("averageLessonProgress", averageLessonProgress != null ? averageLessonProgress : 0.0);

        // Completion rate
        double completionRate = totalLessons > 0 ? (double) completedLessons / totalLessons * 100 : 0.0;
        stats.put("completionRate", completionRate);

        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/user/{userId}/lesson/{lessonId}")
    public ResponseEntity<Void> resetLessonProgress(@PathVariable Long userId, @PathVariable Long lessonId) {
        userLessonProgressRepository.findByUserIdAndLessonId(userId, lessonId)
                .ifPresent(progress -> {
                    progress.setCompletionPercentage(0.0);
                    progress.setStatus(ProgressStatus.NOT_STARTED);
                    userLessonProgressRepository.save(progress);
                });
        return ResponseEntity.ok().build();
    }
}
