package com.eleng.englishback.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_progress")
public class UserProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)  // ✅ bổ sung để fix lỗi
    private Lesson lesson;

    private Integer score;        // ✅ thêm nếu bạn cần lưu điểm
    private Boolean completed;    // ✅ đánh dấu hoàn thành

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
