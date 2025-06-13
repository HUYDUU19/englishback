package com.eleng.englishback.repository;

import com.eleng.englishback.domain.PremiumFeature;
import com.eleng.englishback.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PremiumFeatureRepository extends JpaRepository<PremiumFeature, Long> {

    Optional<PremiumFeature> findByFeatureName(String featureName);

    List<PremiumFeature> findByRequiredRole(Role requiredRole);

    List<PremiumFeature> findByIsActiveTrue();

    @Query("SELECT pf FROM PremiumFeature pf WHERE pf.isActive = true AND (pf.requiredRole = ?1 OR ?1 = 'ADMIN')")
    List<PremiumFeature> findAccessibleFeatures(Role userRole);

}
