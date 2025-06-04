// src/main/java/com/eleng/englishback/repository/UserRepository.java
package com.eleng.englishback.repository;

import com.eleng.englishback.domain.User;
import com.eleng.englishback.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByRole(Role role);

    Long countByRole(Role role);
}