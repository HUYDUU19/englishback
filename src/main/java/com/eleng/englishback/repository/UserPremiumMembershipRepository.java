package com.eleng.englishback.repository;

import com.eleng.englishback.domain.UserPremiumMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserPremiumMembershipRepository extends JpaRepository<UserPremiumMembership, Long> {

    Optional<UserPremiumMembership> findByUserId(Long userId);

    Optional<UserPremiumMembership> findByUserIdAndActiveTrue(Long userId);

    Optional<UserPremiumMembership> findActiveByUserId(Long userId);

}

