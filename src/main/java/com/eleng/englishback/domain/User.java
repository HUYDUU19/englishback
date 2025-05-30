package com.eleng.englishback.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;
import org.hibernate.annotations.*;

import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    @Column(name = "password_hash")
    private String passwordHash;
    private String email;
    private String fullName;
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserProgress> progress;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserMembership> memberships;

    public String getPasswordHash() {
        return this.passwordHash;
    }
}
