package com.eleng.englishback.controller;

import com.eleng.englishback.domain.FlashcardSet;
import com.eleng.englishback.domain.FlashcardCard;
import com.eleng.englishback.repository.FlashcardSetRepository;
import com.eleng.englishback.repository.FlashcardCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/flashcards")
@CrossOrigin(origins = "*")
public class FlashcardController {

    @Autowired
    private FlashcardSetRepository flashcardSetRepository;

    @Autowired
    private FlashcardCardRepository flashcardCardRepository;

    // Flashcard Set endpoints
    @GetMapping("/sets")
    public ResponseEntity<List<FlashcardSet>> getAllSets(
            @RequestParam(required = false) String level,
            @RequestParam(required = false, defaultValue = "false") boolean premiumAccess) {

        List<FlashcardSet> sets;
        if (level != null) {
            sets = flashcardSetRepository.findByLevelAndAccess(level, premiumAccess);
        } else {
            sets = flashcardSetRepository.findAccessibleSets(premiumAccess);
        }
        return ResponseEntity.ok(sets);
    }

    @GetMapping("/sets/{id}")
    public ResponseEntity<FlashcardSet> getSetById(@PathVariable Long id) {
        Optional<FlashcardSet> set = flashcardSetRepository.findByIdAndIsActiveTrue(id);
        return set.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sets/free")
    public ResponseEntity<List<FlashcardSet>> getFreeSets() {
        List<FlashcardSet> freeSets = flashcardSetRepository.findByIsPremiumFalse();
        return ResponseEntity.ok(freeSets);
    }

    @GetMapping("/sets/premium")
    public ResponseEntity<List<FlashcardSet>> getPremiumSets() {
        List<FlashcardSet> premiumSets = flashcardSetRepository.findByIsPremiumAndIsActiveTrue(true);
        return ResponseEntity.ok(premiumSets);
    }

    @GetMapping("/sets/level/{level}")
    public ResponseEntity<List<FlashcardSet>> getSetsByLevel(
            @PathVariable String level,
            @RequestParam(required = false, defaultValue = "false") boolean premiumAccess) {
        List<FlashcardSet> sets = flashcardSetRepository.findByLevelAndAccess(level, premiumAccess);
        return ResponseEntity.ok(sets);
    }

    @GetMapping("/sets/search")
    public ResponseEntity<List<FlashcardSet>> searchSets(@RequestParam String keyword) {
        List<FlashcardSet> sets = flashcardSetRepository.findByNameContainingAndIsActiveTrue(keyword);
        return ResponseEntity.ok(sets);
    }

    @PostMapping("/sets")
    public ResponseEntity<FlashcardSet> createSet(@RequestBody FlashcardSet set) {
        set.setIsActive(true);
        FlashcardSet savedSet = flashcardSetRepository.save(set);
        return ResponseEntity.ok(savedSet);
    }

    @PutMapping("/sets/{id}")
    public ResponseEntity<FlashcardSet> updateSet(@PathVariable Long id, @RequestBody FlashcardSet setDetails) {
        Optional<FlashcardSet> optionalSet = flashcardSetRepository.findById(id);
        if (optionalSet.isPresent()) {
            FlashcardSet set = optionalSet.get();
            set.setName(setDetails.getName());
            set.setDescription(setDetails.getDescription());
            set.setLevel(setDetails.getLevel());
            set.setIsPremium(setDetails.getIsPremium());
            set.setIsActive(setDetails.getIsActive());

            FlashcardSet updatedSet = flashcardSetRepository.save(set);
            return ResponseEntity.ok(updatedSet);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/sets/{id}")
    public ResponseEntity<Void> deleteSet(@PathVariable Long id) {
        Optional<FlashcardSet> set = flashcardSetRepository.findById(id);
        if (set.isPresent()) {
            FlashcardSet flashcardSet = set.get();
            flashcardSet.setIsActive(false);
            flashcardSetRepository.save(flashcardSet);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Flashcard Card endpoints
    @GetMapping("/sets/{setId}/cards")
    public ResponseEntity<List<FlashcardCard>> getCardsBySet(@PathVariable Long setId) {
        List<FlashcardCard> cards = flashcardCardRepository.findByFlashcardSetIdOrderByIdAsc(setId);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/cards/{id}")
    public ResponseEntity<FlashcardCard> getCardById(@PathVariable Long id) {
        Optional<FlashcardCard> card = flashcardCardRepository.findById(id);
        return card.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sets/{setId}/cards/category/{category}")
    public ResponseEntity<List<FlashcardCard>> getCardsByCategory(
            @PathVariable Long setId,
            @PathVariable String category) {
        List<FlashcardCard> cards = flashcardCardRepository.findBySetIdAndCategory(setId, category);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/cards/search")
    public ResponseEntity<List<FlashcardCard>> searchCards(@RequestParam String keyword) {
        List<FlashcardCard> cards = flashcardCardRepository.findByKeyword(keyword);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/sets/{setId}/cards/search")
    public ResponseEntity<List<FlashcardCard>> searchCardsInSet(
            @PathVariable Long setId,
            @RequestParam String keyword) {
        List<FlashcardCard> cards = flashcardCardRepository.findBySetIdAndKeyword(setId, keyword);
        return ResponseEntity.ok(cards);
    }

    @PostMapping("/sets/{setId}/cards")
    public ResponseEntity<FlashcardCard> createCard(@PathVariable Long setId, @RequestBody FlashcardCard card) {
        card.setFlashcardSetId(setId);
        FlashcardCard savedCard = flashcardCardRepository.save(card);
        return ResponseEntity.ok(savedCard);
    }

    @PutMapping("/cards/{id}")
    public ResponseEntity<FlashcardCard> updateCard(@PathVariable Long id, @RequestBody FlashcardCard cardDetails) {
        Optional<FlashcardCard> optionalCard = flashcardCardRepository.findById(id);
        if (optionalCard.isPresent()) {
            FlashcardCard card = optionalCard.get();
            card.setFront(cardDetails.getFront());
            card.setBack(cardDetails.getBack());
            card.setHint(cardDetails.getHint());
            card.setCategory(cardDetails.getCategory());
            card.setAudioUrl(cardDetails.getAudioUrl());
            card.setImageUrl(cardDetails.getImageUrl());

            FlashcardCard updatedCard = flashcardCardRepository.save(card);
            return ResponseEntity.ok(updatedCard);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/cards/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        if (flashcardCardRepository.existsById(id)) {
            flashcardCardRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/sets/{setId}/count")
    public ResponseEntity<Long> getCardCount(@PathVariable Long setId) {
        Long count = flashcardCardRepository.countByFlashcardSetId(setId);
        return ResponseEntity.ok(count);
    }
}
