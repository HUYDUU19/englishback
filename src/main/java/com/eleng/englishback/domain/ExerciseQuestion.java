package com.eleng.englishback.domain;

import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "exercise_question")
public class ExerciseQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText;

    @Column(length = 2000)
    private String options; // JSON: ["A", "B", "C", "D"]

    private String correctAnswer;
    private String audioUrl;
    private String imageUrl;
    private String type;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    @JsonBackReference

    private Exercise exercise;
}
