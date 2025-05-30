package com.eleng.englishback.controller;

import lombok.*;

import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.eleng.englishback.domain.Flashcard;
import com.eleng.englishback.repository.FlashcardRepository;
import java.util.*;;
 
@RestController
@RequestMapping("/api/flashcards")
@RequiredArgsConstructor
public class FlashcardController {
     private final FlashcardRepository flashcardRepository;

    @GetMapping("/random")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Flashcard> getRandomFlashcard() {
        List<Flashcard> all = flashcardRepository.findAll();
        if (all.isEmpty()) return ResponseEntity.noContent().build();
        Flashcard random = all.get(new Random().nextInt(all.size()));
        return ResponseEntity.ok(random);
    }
}

