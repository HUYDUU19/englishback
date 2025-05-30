package com.eleng.englishback.repository;

import com.eleng.englishback.domain.UserMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface UserMembershipRepository extends JpaRepository<UserMembership, Long> {
    List<UserMembership> findByUserId(Long userId);
    // You can add custom query methods here if needed
}
