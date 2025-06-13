package com.eleng.englishback.repository;

import com.eleng.englishback.domain.UserUsageStats;

import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserUsageStatsRepository extends JpaRepository<UserUsageStats, Long> {

    Optional<UserUsageStats> findById(Long id);

    void saveOrUpdateUserStats(Long userId, int correctAnswers, int totalQuestions);

    UserUsageStats findByUserId(Long userId);

    void deleteByUserId(Long userId);

    boolean existsByUserId(Long userId);

    long countByUserId(Long userId);

    List<UserUsageStats> findAll();

}