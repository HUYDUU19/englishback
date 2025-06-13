package com.eleng.englishback.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;



@Entity
@Table(name = "user_premium_features")
public class UserPremiumFeature {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "feature_name", nullable = false)
    private String featureName;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    @Column(name = "activated_at")
    private LocalDateTime activatedAt;
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public UserPremiumFeature() {}
    
    public UserPremiumFeature(Long userId, String featureName, Boolean isActive) {
        this.userId = userId;
        this.featureName = featureName;
        this.isActive = isActive;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getFeatureName() {
        return featureName;
    }
    
    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public LocalDateTime getActivatedAt() {
        return activatedAt;
    }
    
    public void setActivatedAt(LocalDateTime activatedAt) {
        this.activatedAt = activatedAt;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Business methods
    public boolean isFeatureActive() {
        if (!isActive) {
            return false;
        }
        
        if (expiresAt != null) {
            return LocalDateTime.now().isBefore(expiresAt);
        }
        
        return true;
    }
    
    public void activateFeature() {
        this.isActive = true;
        this.activatedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void deactivateFeature() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}