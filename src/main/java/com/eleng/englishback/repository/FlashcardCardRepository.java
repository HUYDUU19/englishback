package com.eleng.englishback.repository;

import com.eleng.englishback.domain.FlashcardCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashcardCardRepository extends JpaRepository<FlashcardCard, Long> {

    List<FlashcardCard> findByFlashcardSetId(Long flashcardSetId);

    List<FlashcardCard> findByFlashcardSetIdOrderByIdAsc(Long flashcardSetId);

    List<FlashcardCard> findByCategory(String category);

    @Query("SELECT fc FROM FlashcardCard fc WHERE fc.flashcardSet.id = :setId AND fc.category = :category")
    List<FlashcardCard> findBySetIdAndCategory(@Param("setId") Long setId, @Param("category") String category);

    @Query("SELECT fc FROM FlashcardCard fc WHERE fc.flashcardSet.id = :setId AND fc.audioUrl IS NOT NULL")
    List<FlashcardCard> findBySetIdWithAudio(@Param("setId") Long setId);

    @Query("SELECT fc FROM FlashcardCard fc WHERE fc.flashcardSet.id = :setId AND fc.imageUrl IS NOT NULL")
    List<FlashcardCard> findBySetIdWithImage(@Param("setId") Long setId);

    @Query("SELECT fc FROM FlashcardCard fc WHERE fc.front LIKE %:keyword% OR fc.back LIKE %:keyword%")
    List<FlashcardCard> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT fc FROM FlashcardCard fc WHERE fc.flashcardSet.id = :setId AND (fc.front LIKE %:keyword% OR fc.back LIKE %:keyword%)")
    List<FlashcardCard> findBySetIdAndKeyword(@Param("setId") Long setId, @Param("keyword") String keyword);

    @Query("SELECT COUNT(fc) FROM FlashcardCard fc WHERE fc.flashcardSet.id = :setId")
    Long countByFlashcardSetId(@Param("setId") Long setId);
}
