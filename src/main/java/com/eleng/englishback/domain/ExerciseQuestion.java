package com.eleng.englishback.domain;

import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data
@Table(name = "exercise_question")
public class ExerciseQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String questionText;

    @Column(length = 2000)
    private String options = "[]"; // Default to empty JSON array

    @NotBlank
    private String correctAnswer;
    
    private String audioUrl; // Can be null
    private String imageUrl; // Can be null
    
    @NotBlank
    private String type;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    @JsonBackReference
    private Exercise exercise;

    // Ensure options is never null when serialized
    @JsonGetter("options")
    public String getOptionsJson() {
        return options != null ? options : "[]";
    }
    
    // Add getters for nullable fields to ensure they're never undefined
    @JsonGetter("audioUrl")
    public String getAudioUrlSafe() {
        return audioUrl;
    }
    
    @JsonGetter("imageUrl")
    public String getImageUrlSafe() {
        return imageUrl;
    }
}
