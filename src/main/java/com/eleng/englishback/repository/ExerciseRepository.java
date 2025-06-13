package com.eleng.englishback.repository;

import com.eleng.englishback.domain.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    List<Exercise> findByLessonId(Long lessonId);

    List<Exercise> findByIsActiveTrue();

    List<Exercise> findByType(String type);

    List<Exercise> findByLevel(String level);

    List<Exercise> findByIsPremium(boolean isPremium);

    @Query("SELECT e FROM Exercise e WHERE e.lessonId = :lessonId AND e.isActive = true")
    List<Exercise> findActiveLessonExercises(@Param("lessonId") Long lessonId);

    @Query("SELECT e FROM Exercise e WHERE e.isActive = true AND (:isPremium = true OR e.isPremium = false)")
    List<Exercise> findAccessibleExercises(@Param("isPremium") boolean isPremium);

    @Query("SELECT e FROM Exercise e WHERE e.level = :level AND e.isActive = true")
    List<Exercise> findByLevelAndActive(@Param("level") String level);

    List<Exercise> findByCreatedByOrderByCreatedAtDesc(Long createdBy);

    @Query("SELECT COUNT(e) FROM Exercise e WHERE e.createdBy = :userId")
    long countByCreatedBy(@Param("userId") Long userId);

    // Additional methods for controller support
    @Query("SELECT e FROM Exercise e WHERE e.type = :type AND e.level = :level AND e.isActive = true AND (:hasAccess = true OR e.isPremium = false)")
    List<Exercise> findByTypeAndLevelAndAccess(@Param("type") String type, @Param("level") String level,
            @Param("hasAccess") boolean hasAccess);

    @Query("SELECT e FROM Exercise e WHERE e.type = :type AND e.isActive = true AND (:hasAccess = true OR e.isPremium = false)")
    List<Exercise> findByTypeAndAccess(@Param("type") String type, @Param("hasAccess") boolean hasAccess);

    @Query("SELECT e FROM Exercise e WHERE e.level = :level AND e.isActive = true AND (:hasAccess = true OR e.isPremium = false)")
    List<Exercise> findByLevelAndAccess(@Param("level") String level, @Param("hasAccess") boolean hasAccess);

    @Query("SELECT COUNT(e) FROM Exercise e WHERE e.isPremium = true")
    Long countByIsPremiumTrue();

    @Query("SELECT COUNT(e) FROM Exercise e WHERE e.isPremium = false")
    Long countByIsPremiumFalse();
}
