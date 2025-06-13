package com.eleng.englishback.domain;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class UserUsageStats {
    private Long id;
    private String userId;
    private int dailyUsage;
    private int weeklyUsage;
    private int monthlyUsage;
    private int totalUsage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserUsageStats() {
    }

    public UserUsageStats(String userId, int dailyUsage, int weeklyUsage, int monthlyUsage, int totalUsage) {
        this.userId = userId;
        this.dailyUsage = dailyUsage;
        this.weeklyUsage = weeklyUsage;
        this.monthlyUsage = monthlyUsage;
        this.totalUsage = totalUsage;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("dailyUsage", dailyUsage);
        map.put("weeklyUsage", weeklyUsage);
        map.put("monthlyUsage", monthlyUsage);
        map.put("totalUsage", totalUsage);
        return map;
    }
    //create setID method
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getDailyUsage() {
        return dailyUsage;
    }

    public void setDailyUsage(int dailyUsage) {
        this.dailyUsage = dailyUsage;
    }

    public int getWeeklyUsage() {
        return weeklyUsage;
    }

    public void setWeeklyUsage(int weeklyUsage) {
        this.weeklyUsage = weeklyUsage;
    }

    public int getMonthlyUsage() {
        return monthlyUsage;
    }

    public void setMonthlyUsage(int monthlyUsage) {
        this.monthlyUsage = monthlyUsage;
    }

    public int getTotalUsage() {
        return totalUsage;
    }

    public void setTotalUsage(int totalUsage) {
        this.totalUsage = totalUsage;
    }
}
