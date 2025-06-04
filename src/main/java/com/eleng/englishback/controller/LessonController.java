package com.eleng.englishback.controller;

import com.eleng.englishback.domain.Lesson;
import com.eleng.englishback.repository.LessonRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonRepository lessonRepository;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    @GetMapping("/level/{level}")
    @PreAuthorize("hasRole('USER'   )")
    public List<Lesson> getByLevel(@PathVariable String level) {
        return lessonRepository.findByLevel(level);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public Lesson getLessonById(@PathVariable Long id) {
        return lessonRepository.findById(id).orElseThrow();
    }
}
