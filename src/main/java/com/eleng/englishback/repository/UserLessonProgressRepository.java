package com.eleng.englishback.repository;

import com.eleng.englishback.domain.UserLessonProgress;
import com.eleng.englishback.domain.ProgressStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLessonProgressRepository extends JpaRepository<UserLessonProgress, Long> {

    Optional<UserLessonProgress> findByUserIdAndLessonId(Long userId, Long lessonId);

    List<UserLessonProgress> findByUserId(Long userId);

    List<UserLessonProgress> findByLessonId(Long lessonId);

    List<UserLessonProgress> findByUserIdAndStatus(Long userId, ProgressStatus status);

    @Query("SELECT ulp FROM UserLessonProgress ulp WHERE ulp.user.id = :userId AND ulp.status = :status ORDER BY ulp.updatedAt DESC")
    List<UserLessonProgress> findByUserIdAndStatusOrderByUpdatedAtDesc(@Param("userId") Long userId,
            @Param("status") ProgressStatus status);

    @Query("SELECT COUNT(ulp) FROM UserLessonProgress ulp WHERE ulp.user.id = :userId AND ulp.status = :status")
    Long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") ProgressStatus status);

    @Query("SELECT ulp FROM UserLessonProgress ulp WHERE ulp.user.id = :userId ORDER BY ulp.updatedAt DESC")
    List<UserLessonProgress> findByUserIdOrderByUpdatedAtDesc(@Param("userId") Long userId);

    @Query("SELECT ulp FROM UserLessonProgress ulp WHERE ulp.user.id = :userId AND ulp.lesson.level = :level")
    List<UserLessonProgress> findByUserIdAndLessonLevel(@Param("userId") Long userId, @Param("level") String level);

    @Query("SELECT AVG(ulp.completionPercentage) FROM UserLessonProgress ulp WHERE ulp.user.id = :userId")
    Double getAverageProgressByUserId(@Param("userId") Long userId);

    @Query("SELECT ulp FROM UserLessonProgress ulp WHERE ulp.user.id = :userId AND ulp.completionPercentage = 100")
    List<UserLessonProgress> findCompletedLessonsByUserId(@Param("userId") Long userId);

    boolean existsByUserIdAndLessonId(Long userId, Long lessonId);
}
