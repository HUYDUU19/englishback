package com.eleng.englishback.repository;

import com.eleng.englishback.domain.FlashcardSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlashcardSetRepository extends JpaRepository<FlashcardSet, Long> {

    List<FlashcardSet> findByIsActiveTrue();

    List<FlashcardSet> findByLevel(String level);

    List<FlashcardSet> findByIsPremiumFalse();

    @Query("SELECT fs FROM FlashcardSet fs WHERE fs.isActive = true AND (fs.isPremium = false OR :hasAccess = true)")
    List<FlashcardSet> findAccessibleSets(@Param("hasAccess") boolean hasAccess);

    @Query("SELECT fs FROM FlashcardSet fs WHERE fs.isActive = true AND fs.level = :level AND (fs.isPremium = false OR :hasAccess = true)")
    List<FlashcardSet> findByLevelAndAccess(@Param("level") String level, @Param("hasAccess") boolean hasAccess);

    @Query("SELECT fs FROM FlashcardSet fs WHERE fs.isActive = true AND fs.isPremium = :isPremium")
    List<FlashcardSet> findByIsPremiumAndIsActiveTrue(@Param("isPremium") boolean isPremium);

    Optional<FlashcardSet> findByIdAndIsActiveTrue(Long id);

    @Query("SELECT fs FROM FlashcardSet fs WHERE fs.name LIKE %:keyword% AND fs.isActive = true")
    List<FlashcardSet> findByNameContainingAndIsActiveTrue(@Param("keyword") String keyword);

    @Query("SELECT COUNT(fc) FROM FlashcardCard fc WHERE fc.flashcardSet.id = :setId")
    Long countCardsBySetId(@Param("setId") Long setId);
}
