package com.eleng.englishback.domain;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Table;

@Data
@Table(name = "user_memberships")
@Entity
public class UserMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private MembershipPlan plan;
    private LocalDate startDate;
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(updatable = false)
    private LocalDate createdAt = LocalDate.now();

    public enum Status {
        ACTIVE,
        EXPIRED,
        CANCELLED
    }

}
