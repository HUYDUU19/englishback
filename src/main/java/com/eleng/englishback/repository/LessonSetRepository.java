package com.eleng.englishback.repository;

import com.eleng.englishback.domain.LessonSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonSetRepository extends JpaRepository<LessonSet, Long> {

    List<LessonSet> findByLevel(String level);

    List<LessonSet> findByIsPremiumFalse();

    @Query("SELECT ls FROM LessonSet ls WHERE ls.isPremium = false OR :hasAccess = true")
    List<LessonSet> findAccessibleSets(@Param("hasAccess") boolean hasAccess);

    @Query("SELECT ls FROM LessonSet ls WHERE ls.level = :level AND (ls.isPremium = false OR :hasAccess = true)")
    List<LessonSet> findByLevelAndAccess(@Param("level") String level, @Param("hasAccess") boolean hasAccess);

    @Query("SELECT ls FROM LessonSet ls WHERE ls.isPremium = :isPremium")
    List<LessonSet> findByIsPremium(@Param("isPremium") boolean isPremium);

    @Query("SELECT ls FROM LessonSet ls WHERE ls.name LIKE %:keyword%")
    List<LessonSet> findByNameContaining(@Param("keyword") String keyword);

    @Query("SELECT ls FROM LessonSet ls WHERE ls.description LIKE %:keyword%")
    List<LessonSet> findByDescriptionContaining(@Param("keyword") String keyword);

    @Query("SELECT COUNT(lsi) FROM LessonSetItem lsi WHERE lsi.lessonSet.id = :setId")
    Long countLessonsBySetId(@Param("setId") Long setId);

    @Query("SELECT ls FROM LessonSet ls ORDER BY ls.createdAt DESC")
    List<LessonSet> findAllOrderByCreatedAtDesc();

    @Query("SELECT ls FROM LessonSet ls WHERE ls.estimatedDuration <= :maxDuration")
    List<LessonSet> findByMaxDuration(@Param("maxDuration") Integer maxDuration);
}
