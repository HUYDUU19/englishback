package com.eleng.englishback.repository;

import com.eleng.englishback.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByIsActiveTrue();

    List<Lesson> findByLevel(String level);

    List<Lesson> findByIsPremium(boolean isPremium);

    List<Lesson> findByIsActiveTrueOrderByOrderIndex();

    @Query("SELECT l FROM Lesson l WHERE l.isActive = true AND (:isPremium = true OR l.isPremium = false)")
    List<Lesson> findAccessibleLessons(@Param("isPremium") boolean isPremium);

    @Query("SELECT l FROM Lesson l WHERE l.level = :level AND l.isActive = true ORDER BY l.orderIndex")
    List<Lesson> findByLevelOrderByOrderIndex(@Param("level") String level);

    List<Lesson> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.userId = :userId")
    long countByUserId(@Param("userId") Long userId);

    // Additional methods for controller support
    @Query("SELECT l FROM Lesson l WHERE l.level = :level AND l.isActive = true AND (:hasAccess = true OR l.isPremium = false)")
    List<Lesson> findByLevelAndAccess(@Param("level") String level, @Param("hasAccess") boolean hasAccess);

    List<Lesson> findByIsPremiumFalseAndIsActiveTrue();

    List<Lesson> findByIsPremiumTrueAndIsActiveTrue();

    @Query("SELECT l FROM Lesson l WHERE (l.title LIKE %:keyword% OR l.description LIKE %:keyword%) AND l.isActive = true")
    List<Lesson> findByTitleContainingOrDescriptionContaining(@Param("keyword") String keyword,
            @Param("keyword2") String keyword2);

    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.isActive = true")
    Long countByIsActiveTrue();

    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.isPremium = true AND l.isActive = true")
    Long countByIsPremiumTrueAndIsActiveTrue();

    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.isPremium = false AND l.isActive = true")
    Long countByIsPremiumFalseAndIsActiveTrue();
}
