package com.eleng.englishback.service;

import java.util.Optional;

import com.eleng.englishback.domain.UserUsageStats;
import com.eleng.englishback.domain.UserUsageLimit;
import com.eleng.englishback.repository.UserUsageStatsRepository;
import com.eleng.englishback.repository.UserUsageLimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserUsageService {

@Autowired private UserUsageStatsRepository userUsageStatsRepository;

@Autowired private UserUsageLimitRepository userUsageLimitRepository;

// UserUsageStats methods
public List<UserUsageStats>getAllUserUsageStats(){return userUsageStatsRepository.findAll();}

public Optional<UserUsageStats>getUserUsageStatsById(Long id){return userUsageStatsRepository.findById(id);}

public UserUsageStats getUserUsageStatsByUserId(Long userId){return userUsageStatsRepository.findByUserId(userId);}

public UserUsageStats createUserUsageStats(UserUsageStats userUsageStats){userUsageStats.setCreatedAt(LocalDateTime.now());userUsageStats.setUpdatedAt(LocalDateTime.now());return userUsageStatsRepository.save(userUsageStats);}

public UserUsageStats updateUserUsageStats(Long id,UserUsageStats userUsageStats){if(userUsageStatsRepository.existsById(id)){userUsageStats.setId(id);userUsageStats.setUpdatedAt(LocalDateTime.now());return userUsageStatsRepository.save(userUsageStats);}return null;}

public boolean deleteUserUsageStats(Long id){if(userUsageStatsRepository.existsById(id)){userUsageStatsRepository.deleteById(id);return true;}return false;}

// UserUsageLimit methods
public List<UserUsageLimit>getAllUserUsageLimits(){return userUsageLimitRepository.findAll();}

public Optional<UserUsageLimit>getUserUsageLimitById(Long id){return userUsageLimitRepository.findById(id);}

public Optional<UserUsageLimit>getUserUsageLimitsByUserId(Long userId){return userUsageLimitRepository.findByUserId(userId);}

public UserUsageLimit createUserUsageLimit(UserUsageLimit userUsageLimit){userUsageLimit.setCreatedAt(LocalDateTime.now());userUsageLimit.setUpdatedAt(LocalDateTime.now());return userUsageLimitRepository.save(userUsageLimit);}

public UserUsageLimit updateUserUsageLimit(Long id,UserUsageLimit userUsageLimit){if(userUsageLimitRepository.existsById(id)){userUsageLimit.setId(id);userUsageLimit.setUpdatedAt(LocalDateTime.now());return userUsageLimitRepository.save(userUsageLimit);}return null;}

public boolean deleteUserUsageLimit(Long id){if(userUsageLimitRepository.existsById(id)){userUsageLimitRepository.deleteById(id);return true;}return false;}

// Business logic methods
public boolean hasExceededLimit(Long userId,String limitType){UserUsageStats stats=getUserUsageStatsByUserId(userId);Optional<UserUsageLimit>limits=getUserUsageLimitsByUserId(userId);
return false; // TODO: Implement logic to check if user has exceeded limit
}
}


// Logic to increment usage count for a user
// This would update the UserUsageStats entity

