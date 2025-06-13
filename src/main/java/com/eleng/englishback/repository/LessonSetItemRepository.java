package com.eleng.englishback.repository;

import com.eleng.englishback.domain.LessonSetItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonSetItemRepository extends JpaRepository<LessonSetItem, Long> {

    List<LessonSetItem> findByLessonSetId(Long lessonSetId);

    List<LessonSetItem> findByLessonSetIdOrderByOrderIndexAsc(Long lessonSetId);

    List<LessonSetItem> findByLessonId(Long lessonId);

    @Query("SELECT lsi FROM LessonSetItem lsi WHERE lsi.lessonSet.id = :setId AND lsi.orderIndex = :orderIndex")
    Optional<LessonSetItem> findByLessonSetIdAndOrderIndex(@Param("setId") Long setId,
            @Param("orderIndex") Integer orderIndex);

    @Query("SELECT MAX(lsi.orderIndex) FROM LessonSetItem lsi WHERE lsi.lessonSet.id = :setId")
    Optional<Integer> findMaxOrderIndexByLessonSetId(@Param("setId") Long setId);

    @Query("SELECT COUNT(lsi) FROM LessonSetItem lsi WHERE lsi.lessonSet.id = :setId")
    Long countByLessonSetId(@Param("setId") Long setId);

    @Query("SELECT lsi FROM LessonSetItem lsi WHERE lsi.lessonSet.id = :setId AND lsi.orderIndex > :currentIndex ORDER BY lsi.orderIndex ASC")
    List<LessonSetItem> findNextLessonsInSet(@Param("setId") Long setId, @Param("currentIndex") Integer currentIndex);

    @Query("SELECT lsi FROM LessonSetItem lsi WHERE lsi.lessonSet.id = :setId AND lsi.orderIndex < :currentIndex ORDER BY lsi.orderIndex DESC")
    List<LessonSetItem> findPreviousLessonsInSet(@Param("setId") Long setId,
            @Param("currentIndex") Integer currentIndex);

    boolean existsByLessonSetIdAndLessonId(Long lessonSetId, Long lessonId);

    void deleteByLessonSetIdAndLessonId(Long lessonSetId, Long lessonId);
}
