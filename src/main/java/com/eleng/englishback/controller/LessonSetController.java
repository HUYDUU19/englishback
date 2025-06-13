package com.eleng.englishback.controller;

import com.eleng.englishback.domain.LessonSet;
import com.eleng.englishback.domain.LessonSetItem;
import com.eleng.englishback.domain.Lesson;
import com.eleng.englishback.repository.LessonSetRepository;
import com.eleng.englishback.repository.LessonSetItemRepository;
import com.eleng.englishback.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/lesson-sets")
@CrossOrigin(origins = "*")
public class LessonSetController {

    @Autowired
    private LessonSetRepository lessonSetRepository;

    @Autowired
    private LessonSetItemRepository lessonSetItemRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @GetMapping
    public ResponseEntity<List<LessonSet>> getAllLessonSets(
            @RequestParam(required = false) String level,
            @RequestParam(required = false, defaultValue = "false") boolean premiumAccess) {

        List<LessonSet> lessonSets;
        if (level != null) {
            lessonSets = lessonSetRepository.findByLevelAndAccess(level, premiumAccess);
        } else {
            lessonSets = lessonSetRepository.findAccessibleSets(premiumAccess);
        }
        return ResponseEntity.ok(lessonSets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonSet> getLessonSetById(@PathVariable Long id) {
        Optional<LessonSet> lessonSet = lessonSetRepository.findById(id);
        return lessonSet.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/lessons")
    public ResponseEntity<List<Lesson>> getLessonSetLessons(@PathVariable Long id) {
        List<LessonSetItem> items = lessonSetItemRepository.findByLessonSetIdOrderByOrderIndexAsc(id);
        List<Lesson> lessons = items.stream()
                .map(item -> lessonRepository.findById(item.getLessonId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/free")
    public ResponseEntity<List<LessonSet>> getFreeLessonSets() {
        List<LessonSet> freeSets = lessonSetRepository.findByIsPremiumFalse();
        return ResponseEntity.ok(freeSets);
    }

    @GetMapping("/premium")
    public ResponseEntity<List<LessonSet>> getPremiumLessonSets() {
        List<LessonSet> premiumSets = lessonSetRepository.findByIsPremium(true);
        return ResponseEntity.ok(premiumSets);
    }

    @GetMapping("/level/{level}")
    public ResponseEntity<List<LessonSet>> getLessonSetsByLevel(
            @PathVariable String level,
            @RequestParam(required = false, defaultValue = "false") boolean premiumAccess) {
        List<LessonSet> lessonSets = lessonSetRepository.findByLevelAndAccess(level, premiumAccess);
        return ResponseEntity.ok(lessonSets);
    }

    @PostMapping
    public ResponseEntity<LessonSet> createLessonSet(@RequestBody LessonSet lessonSet) {
        LessonSet savedSet = lessonSetRepository.save(lessonSet);
        return ResponseEntity.ok(savedSet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonSet> updateLessonSet(@PathVariable Long id, @RequestBody LessonSet setDetails) {
        Optional<LessonSet> optionalSet = lessonSetRepository.findById(id);
        if (optionalSet.isPresent()) {
            LessonSet lessonSet = optionalSet.get();
            lessonSet.setName(setDetails.getName());
            lessonSet.setDescription(setDetails.getDescription());
            lessonSet.setLevel(setDetails.getLevel());
            lessonSet.setIsPremium(setDetails.getIsPremium());
            lessonSet.setEstimatedDuration(setDetails.getEstimatedDuration());

            LessonSet updatedSet = lessonSetRepository.save(lessonSet);
            return ResponseEntity.ok(updatedSet);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLessonSet(@PathVariable Long id) {
        if (lessonSetRepository.existsById(id)) {
            lessonSetRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{setId}/lessons/{lessonId}")
    public ResponseEntity<LessonSetItem> addLessonToSet(
            @PathVariable Long setId,
            @PathVariable Long lessonId,
            @RequestParam(required = false) Integer orderIndex) {

        // Check if lesson is already in the set
        if (lessonSetItemRepository.existsByLessonSetIdAndLessonId(setId, lessonId)) {
            return ResponseEntity.badRequest().build();
        }

        LessonSetItem item = new LessonSetItem();
        item.setLessonSetId(setId);
        item.setLessonId(lessonId);

        if (orderIndex == null) {
            // Get the next order index
            Optional<Integer> maxIndex = lessonSetItemRepository.findMaxOrderIndexByLessonSetId(setId);
            item.setOrderIndex(maxIndex.orElse(0) + 1);
        } else {
            item.setOrderIndex(orderIndex);
        }

        LessonSetItem savedItem = lessonSetItemRepository.save(item);
        return ResponseEntity.ok(savedItem);
    }

    @DeleteMapping("/{setId}/lessons/{lessonId}")
    public ResponseEntity<Void> removeLessonFromSet(@PathVariable Long setId, @PathVariable Long lessonId) {
        lessonSetItemRepository.deleteByLessonSetIdAndLessonId(setId, lessonId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{setId}/lessons/{lessonId}/order")
    public ResponseEntity<LessonSetItem> updateLessonOrder(
            @PathVariable Long setId,
            @PathVariable Long lessonId,
            @RequestBody Map<String, Integer> orderData) {

        Optional<LessonSetItem> itemOptional = lessonSetItemRepository
                .findByLessonSetIdAndOrderIndex(setId, orderData.get("oldOrder"));

        if (itemOptional.isPresent()) {
            LessonSetItem item = itemOptional.get();
            item.setOrderIndex(orderData.get("newOrder"));
            LessonSetItem updatedItem = lessonSetItemRepository.save(item);
            return ResponseEntity.ok(updatedItem);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<LessonSet>> searchLessonSets(@RequestParam String keyword) {
        List<LessonSet> sets = lessonSetRepository.findByNameContaining(keyword);
        return ResponseEntity.ok(sets);
    }

    @GetMapping("/{id}/count")
    public ResponseEntity<Long> getLessonCount(@PathVariable Long id) {
        Long count = lessonSetRepository.countLessonsBySetId(id);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/duration/{maxDuration}")
    public ResponseEntity<List<LessonSet>> getLessonSetsByMaxDuration(@PathVariable Integer maxDuration) {
        List<LessonSet> sets = lessonSetRepository.findByMaxDuration(maxDuration);
        return ResponseEntity.ok(sets);
    }
}
