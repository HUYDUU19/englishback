package com.eleng.englishback.service;

import com.eleng.englishback.domain.PremiumFeature;
import com.eleng.englishback.repository.PremiumFeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.eleng.englishback.domain.UserUsageStats;
import com.eleng.englishback.domain.UserPremiumFeature;

@Service
public class PremiumService {

    @Autowired
    private PremiumFeatureRepository premiumFeatureRepository;

    public List<PremiumFeature> getAllPremiums() {
        return premiumFeatureRepository.findAll();
    }

    public Optional<PremiumFeature> getPremiumById(Long id) {
        return premiumFeatureRepository.findById(id);
    }

    public PremiumFeature createPremium(PremiumFeature premium) {
        return premiumFeatureRepository.save(premium);
    }

    public PremiumFeature updatePremium(Long id, PremiumFeature premiumDetails) {
        PremiumFeature premium = premiumFeatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Premium not found with id: " + id));

        // Update fields as needed
        premium.setName(premiumDetails.getName());
        premium.setDescription(premiumDetails.getDescription());
        premium.setPrice(premiumDetails.getPrice());
        premium.setDuration(premiumDetails.getDuration());

        return premiumFeatureRepository.save(premium);
    }

    public void deletePremium(Long id) {
        PremiumFeature premium = premiumFeatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Premium not found with id: " + id));
        premiumFeatureRepository.delete(premium);
    }

    public boolean existsById(Long id) {
        return premiumFeatureRepository.existsById(id);
    }

    public long count() {
        return premiumFeatureRepository.count();
    }

    public PremiumService() {
    }


    // create getPrice
    public String getPrice(Long id) {
        PremiumFeature premium = premiumFeatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Premium not found with id: " + id));
        return premium.getPrice();
    }

    // create getDuration
    public String getDuration(Long id) {
        PremiumFeature premium = premiumFeatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Premium not found with id: " + id));
        return premium.getDuration();
    }

    // create hasUserPremiumAccess method
    public boolean hasUserPremiumAccess(Long userId) {
        // Logic to check if the user has premium access
        // This could involve checking the user's role, premium status, etc.
        // For example:
        return premiumFeatureRepository.existsById(userId);
    }

    // create getUserUsageStats method
    public UserUsageStats getUserUsageStats(Long userId) {
        // Logic to retrieve user usage statistics
        // This could involve querying the database for the user's activity related to
        // premium features
        UserUsageStats userStats = new UserUsageStats();
        userStats.setUserId(userId.toString());
        return userStats;

    }

    // create getUserPremiumFeatures method
    public List<UserPremiumFeature> getUserPremiumFeatures(Long userId) {
        // Logic to retrieve user's premium features
        // For now, returning an empty list as placeholder
        return List.of();
    }

    // create canUserAccessFeature method
    public boolean canUserAccessFeature(Long userId, String featureName) {
        // Logic to check if user can access a specific feature
        return hasUserPremiumAccess(userId);
    }

    // create hasUserReachedExerciseLimit method
    public boolean hasUserReachedExerciseLimit(Long userId) {
        // Logic to check if user has reached exercise limit
        // For now, returning false as placeholder
        return false;
    }

    // create incrementExerciseUsage method
    public void incrementExerciseUsage(Long userId) {
        // Logic to increment user's exercise usage count
        // Placeholder implementation
    }

    // create hasUserReachedVocabularyLimit method
    public boolean hasUserReachedVocabularyLimit(Long userId) {
        // Logic to check if user has reached vocabulary limit
        // For now, returning false as placeholder
        return false;
    }

    // create incrementVocabularyUsage method
    public void incrementVocabularyUsage(Long userId) {
        // Logic to increment user's vocabulary usage count
        // Placeholder implementation
    }

    // create upgradeToPremium method
    public boolean upgradeToPremium(Long userId) {
        // Logic to upgrade user to premium
        // For now, returning true as placeholder
        return true;
    }

    // create downgradeToBasic method
    public boolean downgradeToBasic(Long userId) {
        // Logic to downgrade user to basic
        // For now, returning true as placeholder
        return true;
    }

}