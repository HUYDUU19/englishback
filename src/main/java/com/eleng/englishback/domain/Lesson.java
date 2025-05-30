package com.eleng.englishback.domain;
import lombok.*;
import jakarta.persistence.*;
import java.util.*;


import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Column(length = 5)
    private String level;  // A1, A2, B1, B2, C1, C2

    @JsonIgnore
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exercise> exercises;
    
}
