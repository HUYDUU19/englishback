package com.eleng.englishback.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eleng.englishback.domain.Role;
import com.eleng.englishback.domain.User;
import com.eleng.englishback.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Tạo admin user đầu tiên cho hệ thống
     */
    public User createDefaultAdmin() {
        // Kiểm tra xem đã có admin nào chưa
        if (userRepository.existsByRole(Role.ADMIN)) {
            throw new RuntimeException("Admin user already exists");
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setPasswordHash(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@leenglish.com");
        admin.setFullName("System Administrator");
        admin.setRole(Role.ADMIN);
        admin.setActive(true);

        return userRepository.save(admin);
    }

    /**
     * Tạo user thường
     */
    public User createUser(String username, String password, String email, String fullName) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setFullName(fullName);
        user.setRole(Role.USER);
        user.setActive(true);

        return userRepository.save(user);
    }

    /**
     * Tạo admin user
     */
    public User createAdmin(String username, String password, String email, String fullName) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        User admin = new User();
        admin.setUsername(username);
        admin.setPasswordHash(passwordEncoder.encode(password));
        admin.setEmail(email);
        admin.setFullName(fullName);
        admin.setRole(Role.ADMIN);
        admin.setActive(true);

        return userRepository.save(admin);
    }
}
