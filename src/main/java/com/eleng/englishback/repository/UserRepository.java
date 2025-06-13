package com.eleng.englishback.repository;

import com.eleng.englishback.domain.User;
import com.eleng.englishback.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean exexistsByRole(Role role);

    List<User> findByRole(Role role);

    List<User> findByIsPremium(boolean isPremium);

    @Query("SELECT u FROM User u WHERE u.role = 'USER' AND u.isPremium = true")
    List<User> findPremiumUsers();

    @Query("SELECT u FROM User u WHERE u.role = 'USER' AND u.isPremium = false")
    List<User> findFreeUsers();

    @Query("SELECT u FROM User u WHERE u.isActive = true")
    List<User> findActiveUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    long countByRole(@Param("role") Role role);

    List<User> findByIsPremiumTrue();

    long countByIsPremiumTrue();

    long countByIsActiveTrue();

    List<User> findByIsActiveTrue(Boolean isActive);

    List<User> findByUsernameContainingOrEmailContainingOrFullNameContaining(
            String username, String email, String fullName);

}
