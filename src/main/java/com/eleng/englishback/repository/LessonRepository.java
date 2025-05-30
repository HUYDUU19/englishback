package com.eleng.englishback.repository;

import com.eleng.englishback.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByLevel(String level);
}
