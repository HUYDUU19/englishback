package com.eleng.englishback.repository;

import com.eleng.englishback.domain.User;
import com.eleng.englishback.domain.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    List<UserProgress> findByUser(User user);
   
    // Nếu cần tìm theo lesson, hãy thêm trường lesson vào UserProgress entity trước
}
