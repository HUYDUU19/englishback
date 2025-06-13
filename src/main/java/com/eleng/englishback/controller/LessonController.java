package com.eleng.englishback.controller;

import com.eleng.englishback.domain.Lesson;
import com.eleng.englishback.domain.UserLessonProgress;
import com.eleng.englishback.domain.ProgressStatus;
import com.eleng.englishback.repository.LessonRepository;
import com.eleng.englishback.repository.UserLessonProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lessons")
@CrossOrigin(origins = "*")
public class LessonController {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserLessonProgressRepository userLessonProgressRepository;

    @GetMapping
    public ResponseEntity<List<Lesson>> getAllLessons(
            @RequestParam(required = false) String level,
            @RequestParam(required = false, defaultValue = "false") boolean premiumAccess) {

        List<Lesson> lessons;
        if (level != null) {
            lessons = lessonRepository.findByLevelAndAccess(level, premiumAccess);
        } else {
            lessons = lessonRepository.findAccessibleLessons(premiumAccess);
        }
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable Long id) {
        Optional<Lesson> lesson = lessonRepository.findById(id);
        return lesson.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/free")
    public ResponseEntity<List<Lesson>> getFreeLessons() {
        List<Lesson> freeLessons = lessonRepository.findByIsPremiumFalseAndIsActiveTrue();
        return ResponseEntity.ok(freeLessons);
    }

    @GetMapping("/premium")
    public ResponseEntity<List<Lesson>> getPremiumLessons() {
        List<Lesson> premiumLessons = lessonRepository.findByIsPremiumTrueAndIsActiveTrue();
        return ResponseEntity.ok(premiumLessons);
    }

    @GetMapping("/level/{level}")
    public ResponseEntity<List<Lesson>> getLessonsByLevel(
            @PathVariable String level,
            @RequestParam(required = false, defaultValue = "false") boolean premiumAccess) {
        List<Lesson> lessons = lessonRepository.findByLevelAndAccess(level, premiumAccess);
        return ResponseEntity.ok(lessons);
    }

    @PostMapping
    public ResponseEntity<Lesson> createLesson(@RequestBody Lesson lesson) {
        Lesson savedLesson = lessonRepository.save(lesson);
        return ResponseEntity.ok(savedLesson);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lesson> updateLesson(@PathVariable Long id, @RequestBody Lesson lessonDetails) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(id);
        if (optionalLesson.isPresent()) {
            Lesson lesson = optionalLesson.get();
            lesson.setTitle(lessonDetails.getTitle());
            lesson.setDescription(lessonDetails.getDescription());
            lesson.setContent(lessonDetails.getContent());
            lesson.setLevel(lessonDetails.getLevel());
            lesson.setIsPremium(lessonDetails.getIsPremium());
            lesson.setIsActive(lessonDetails.getIsActive());
            lesson.setOrderIndex(lessonDetails.getOrderIndex());

            Lesson updatedLesson = lessonRepository.save(lesson);
            return ResponseEntity.ok(updatedLesson);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        if (lessonRepository.existsById(id)) {
            lessonRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Lesson>> searchLessons(@RequestParam String keyword) {
        List<Lesson> lessons = lessonRepository.findByTitleContainingOrDescriptionContaining(keyword, keyword);
        return ResponseEntity.ok(lessons);
    }

    // Progress tracking endpoints @PostMapping("/{lessonId}/progress/{userId}")
    public ResponseEntity<UserLessonProgress> updateProgress(
            @PathVariable Long lessonId,
            @PathVariable Long userId,
            @RequestParam double completionPercentage,
            @RequestParam ProgressStatus status) {

        Optional<UserLessonProgress> existingProgress = userLessonProgressRepository.findByUserIdAndLessonId(userId,
                lessonId);

        UserLessonProgress progress;
        if (existingProgress.isPresent()) {
            progress = existingProgress.get();
            progress.setCompletionPercentage(completionPercentage);
            progress.setStatus(status);
        } else {
            progress = new UserLessonProgress();
            progress.setUserId(userId);
            progress.setLessonId(lessonId);
            progress.setCompletionPercentage(completionPercentage);
            progress.setStatus(status);
        }

        UserLessonProgress savedProgress = userLessonProgressRepository.save(progress);
        return ResponseEntity.ok(savedProgress);
    }

    @GetMapping("/{lessonId}/progress/{userId}")
    public ResponseEntity<UserLessonProgress> getProgress(@PathVariable Long lessonId, @PathVariable Long userId) {
        Optional<UserLessonProgress> progress = userLessonProgressRepository.findByUserIdAndLessonId(userId, lessonId);
        return progress.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
