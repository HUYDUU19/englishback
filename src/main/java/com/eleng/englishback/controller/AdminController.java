package com.eleng.englishback.controller;

import com.eleng.englishback.domain.User;
import com.eleng.englishback.domain.Role;
import com.eleng.englishback.domain.Lesson;
import com.eleng.englishback.domain.Exercise;
import com.eleng.englishback.domain.FlashcardSet;
import com.eleng.englishback.repository.UserRepository;
import com.eleng.englishback.repository.LessonRepository;
import com.eleng.englishback.repository.ExerciseRepository;
import com.eleng.englishback.repository.FlashcardSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private FlashcardSetRepository flashcardSetRepository;

    // User management
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable Role role) {
        List<User> users = userRepository.findByRole(role);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/premium")
    public ResponseEntity<List<User>> getPremiumUsers() {
        List<User> users = userRepository.findByIsPremiumTrue();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/active")
    public ResponseEntity<List<User>> getActiveUsers() {
        List<User> users = userRepository.findByIsActiveTrue(null);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<User> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> roleData) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Role newRole = Role.valueOf(roleData.get("role"));
            user.setRole(newRole);
            User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/users/{id}/premium")
    public ResponseEntity<User> updateUserPremiumStatus(@PathVariable Long id,
            @RequestBody Map<String, Boolean> premiumData) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setIsPremium(premiumData.get("isPremium"));
            User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/users/{id}/active")
    public ResponseEntity<User> updateUserActiveStatus(@PathVariable Long id,
            @RequestBody Map<String, Boolean> activeData) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setIsActive(activeData.get("isActive"));
            User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Content management
    @GetMapping("/lessons")
    public ResponseEntity<List<Lesson>> getAllLessonsForAdmin() {
        List<Lesson> lessons = lessonRepository.findAll();
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/exercises")
    public ResponseEntity<List<Exercise>> getAllExercisesForAdmin() {
        List<Exercise> exercises = exerciseRepository.findAll();
        return ResponseEntity.ok(exercises);
    }

    @GetMapping("/flashcard-sets")
    public ResponseEntity<List<FlashcardSet>> getAllFlashcardSetsForAdmin() {
        List<FlashcardSet> sets = flashcardSetRepository.findAll();
        return ResponseEntity.ok(sets);
    }

    @PutMapping("/lessons/{id}/premium")
    public ResponseEntity<Lesson> updateLessonPremiumStatus(@PathVariable Long id,
            @RequestBody Map<String, Boolean> premiumData) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(id);
        if (lessonOptional.isPresent()) {
            Lesson lesson = lessonOptional.get();
            lesson.setIsPremium(premiumData.get("isPremium"));
            Lesson updatedLesson = lessonRepository.save(lesson);
            return ResponseEntity.ok(updatedLesson);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/exercises/{id}/premium")
    public ResponseEntity<Exercise> updateExercisePremiumStatus(@PathVariable Long id,
            @RequestBody Map<String, Boolean> premiumData) {
        Optional<Exercise> exerciseOptional = exerciseRepository.findById(id);
        if (exerciseOptional.isPresent()) {
            Exercise exercise = exerciseOptional.get();
            exercise.setIsPremium(premiumData.get("isPremium"));
            Exercise updatedExercise = exerciseRepository.save(exercise);
            return ResponseEntity.ok(updatedExercise);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/flashcard-sets/{id}/premium")
    public ResponseEntity<FlashcardSet> updateFlashcardSetPremiumStatus(@PathVariable Long id,
            @RequestBody Map<String, Boolean> premiumData) {
        Optional<FlashcardSet> setOptional = flashcardSetRepository.findById(id);
        if (setOptional.isPresent()) {
            FlashcardSet set = setOptional.get();
            set.setIsPremium(premiumData.get("isPremium"));
            FlashcardSet updatedSet = flashcardSetRepository.save(set);
            return ResponseEntity.ok(updatedSet);
        }
        return ResponseEntity.notFound().build();
    }

    // Statistics and dashboard
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();

        // User statistics
        Long totalUsers = userRepository.count();
        Long premiumUsers = userRepository.countByIsPremiumTrue();
        Long activeUsers = userRepository.countByIsActiveTrue();
        Long adminUsers = userRepository.countByRole(Role.ADMIN);
        Long collaboratorUsers = userRepository.countByRole(Role.COLLABORATOR);
        Long regularUsers = userRepository.countByRole(Role.USER);

        // Content statistics
        Long totalLessons = lessonRepository.count();
        Long activeLessons = lessonRepository.countByIsActiveTrue();
        Long premiumLessons = lessonRepository.countByIsPremiumTrueAndIsActiveTrue();
        Long freeLessons = lessonRepository.countByIsPremiumFalseAndIsActiveTrue();

        Long totalExercises = exerciseRepository.count();
        Long premiumExercises = exerciseRepository.countByIsPremiumTrue();
        Long freeExercises = exerciseRepository.countByIsPremiumFalse();

        Long totalFlashcardSets = flashcardSetRepository.count();
        Long activeFlashcardSets = (long) flashcardSetRepository.findByIsActiveTrue().size();

        stats.put("users", Map.of(
                "total", totalUsers,
                "premium", premiumUsers,
                "active", activeUsers,
                "admin", adminUsers,
                "collaborator", collaboratorUsers,
                "regular", regularUsers));

        stats.put("content", Map.of(
                "totalLessons", totalLessons,
                "activeLessons", activeLessons,
                "premiumLessons", premiumLessons,
                "freeLessons", freeLessons,
                "totalExercises", totalExercises,
                "premiumExercises", premiumExercises,
                "freeExercises", freeExercises,
                "totalFlashcardSets", totalFlashcardSets,
                "activeFlashcardSets", activeFlashcardSets));

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String keyword) {
        List<User> users = userRepository.findByUsernameContainingOrEmailContainingOrFullNameContaining(keyword,
                keyword, keyword);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users/bulk-update")
    public ResponseEntity<Map<String, Object>> bulkUpdateUsers(@RequestBody Map<String, Object> updateData) {
        @SuppressWarnings("unchecked")
        List<Long> userIds = (List<Long>) updateData.get("userIds");
        String action = (String) updateData.get("action");
        Object value = updateData.get("value");

        int updatedCount = 0;

        for (Long userId : userIds) {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                switch (action) {
                    case "setRole":
                        user.setRole(Role.valueOf((String) value));
                        break;
                    case "setPremium":
                        user.setIsPremium((Boolean) value);
                        break;
                    case "setActive":
                        user.setIsActive((Boolean) value);
                        break;
                }

                userRepository.save(user);
                updatedCount++;
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("updatedCount", updatedCount);
        response.put("message", updatedCount + " users updated successfully");

        return ResponseEntity.ok(response);
    }
}
