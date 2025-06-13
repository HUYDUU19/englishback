package com.eleng.englishback.domain;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "premium_features")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PremiumFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private boolean enabled = true;

    public boolean isAccessibleByRole(Role role) {
        // Add your role-based access logic here
        // For example, you might check if the role has permission to access this
        // feature
        return enabled; // Basic implementation - modify as needed
    }

    public PremiumFeature(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public PremiumFeature(String name, String description, boolean enabled) {
        this.name = name;
        this.description = description;
        this.enabled = enabled;

    }

    // create getPrice method
    public String getPrice() {
        // Placeholder for price logic, modify as needed
        return "Free"; // Default price for demonstration
    }

    // create setPrice method
    public void setPrice(String price) {
        // Placeholder for setting price logic, modify as needed
        // This method can be used to set a price if needed
    }

    // create getDuratiocn method with correct premiumfeaturerepository import
    public String getDuration() {
        // Placeholder for duration logic, modify as needed
        return "Lifetime"; // Default duration for demonstration
    }

    // create setDuration method
    public void setDuration(String duration) {
        // Placeholder for setting duration logic, modify as needed

        // This method can be used to set a duration if needed
        // This method can be used to set a duration if needed
        // Currently no field to store duration, so this is a placeholder
    }

}
