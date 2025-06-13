package com.eleng.englishback.domain;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
// Temporarily commented out due to User class compilation issues
// import com.eleng.englishback.domain.User;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "lesson_sets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 5)
    private String level;
    @Column(name = "is_premium", nullable = false)
    @Builder.Default
    private Boolean isPremium = false;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "order_index")
    @Builder.Default
    private Integer orderIndex = 0;

    @Column(name = "estimated_duration")
    private Integer estimatedDuration; // total duration in minutes

    @Column(name = "user_id")
    private Long userId; // creator

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // One-to-many relationship with LessonSetItem
    @OneToMany(mappedBy = "lessonSet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LessonSetItem> items;

    // Reference to User (creator) - temporarily commented out due to compilation
    // issues
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id", insertable = false, updatable = false)
    // private User user;
}
