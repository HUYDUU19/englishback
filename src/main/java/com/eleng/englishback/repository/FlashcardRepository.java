package com.eleng.englishback.repository;


import com.eleng.englishback.domain.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

    // Lấy tất cả flashcard theo trình độ
    List<Flashcard> findByLevel(String level);
}