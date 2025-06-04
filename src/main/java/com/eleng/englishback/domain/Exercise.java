package com.eleng.englishback.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import com.fasterxml.jackson.annotation.*;

@Entity
@Data
@Table(name = "exercises")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String type; // e.g. multiple-choice, fill-in-the-blank
    private String level;
    private Boolean isPremium = false;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    @JsonManagedReference
    private Lesson lesson;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ExerciseQuestion> questions;
}
