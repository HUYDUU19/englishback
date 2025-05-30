package com.eleng.englishback.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;


@Entity

@Table(name = "membership_plans")
@Data

public class MembershipPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    private String name;
    private String description;
    private Double price;
    private Integer durationDays; // số ngày hiệu lực

    private String features;
    private boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
