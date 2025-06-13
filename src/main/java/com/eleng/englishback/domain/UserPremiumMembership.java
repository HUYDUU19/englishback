package com.eleng.englishback.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_premium_memberships")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPremiumMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Temporarily commented out due to User class compilation issues
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id", nullable = false)
    // private User user;

    @Column(name = "user_id", nullable = false)
    private Long userId; // Temporary workaround to store user ID directly

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MembershipStatus status = MembershipStatus.ACTIVE;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "amount_paid", precision = 10, scale = 2)
    private BigDecimal amountPaid;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum MembershipStatus {
        ACTIVE, EXPIRED, CANCELLED
    }

    // Utility methods
    public boolean isActive() {
        return status == MembershipStatus.ACTIVE && endDate.isAfter(LocalDate.now());
    }

    public boolean isExpired() {
        return endDate.isBefore(LocalDate.now()) || status == MembershipStatus.EXPIRED;
    }

    public long getDaysRemaining() {
        if (isExpired()) {
            return 0;
        }
        return LocalDate.now().until(endDate).getDays();
    }

    public String getMembershipType() {
        return "PREMIUM";
    }
    
}
