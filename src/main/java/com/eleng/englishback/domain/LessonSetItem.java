package com.eleng.englishback.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "lesson_set_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonSetItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lesson_set_id", nullable = false)
    private Long lessonSetId;

    @Column(name = "lesson_id", nullable = false)
    private Long lessonId;

    @Column(name = "order_index")
    private Integer orderIndex = 0;

    // Many-to-one relationship with LessonSet
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_set_id", insertable = false, updatable = false)
    private LessonSet lessonSet;

    // Many-to-one relationship with Lesson
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", insertable = false, updatable = false)
    private Lesson lesson;
}
