package com.eleng.englishback.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordhash; // Changed from passwordHash to password for consistency

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.USER; // Default role

    @Column(name = "is_premium", nullable = false)
    private Boolean isPremium = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "profile_picture_url", length = 500)
    private String profilePictureUrl;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Column(length = 100)
    private String country;

    @Column(name = "preferred_language", length = 10)
    private String preferredLanguage = "en";

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "premium_expires_at")
    private LocalDateTime premiumExpiresAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Utility method to check if user has premium access
    public boolean hasPremiumAccess() {
        return (role == Role.USER && isPremium) ||
                role == Role.COLLABORATOR ||
                role == Role.ADMIN;
    }

    // Utility method to check if user can create content
    public boolean canCreateContent() {
        return role == Role.COLLABORATOR || role == Role.ADMIN;
    }

    // Utility method to check admin privileges
    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    // Utility method to check if user is a basic user (role = USER and not premium)
    public boolean isBasicUser() {
        return role == Role.USER && !isPremium;
    }

    // Utility method to check if user is a premium user (role = USER and has
    // premium)
    public boolean isPremiumUser() {
        return role == Role.USER && isPremium;
    }

    // Utility method to check if user is a collaborator
    public LocalDateTime getPremiumExpiresAt() {
        return this.premiumExpiresAt;

    }

    public Boolean getIsPremium() {
        return isPremium;
    }
    public boolean isCollaborator() {
        return role == Role.COLLABORATOR;
    }
    




}
