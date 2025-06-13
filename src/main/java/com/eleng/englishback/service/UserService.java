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
        if (userRepository.exexistsByRole(Role.ADMIN)) {
            throw new RuntimeException("Admin user already exists");
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setPasswordhash(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@leenglish.com");
        admin.setFullName("System Administrator");
        admin.setRole(Role.ADMIN);
        admin.setIsActive(true);

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
        user.setPasswordhash(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setFullName(fullName);
        user.setRole(Role.USER);
        user.setIsActive(true);

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
        admin.setPasswordhash(passwordEncoder.encode(password));
        admin.setEmail(email);
        admin.setFullName(fullName);
        admin.setRole(Role.ADMIN);
        admin.setIsActive(true);

        return userRepository.save(admin);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    /**
     * Get all users
     */
    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // The method getUserById(Long) is undefined for the type UserService
    public java.util.Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // createUser(User) is undefined for the type UserService
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        user.setPasswordhash(passwordEncoder.encode(user.getPasswordhash()));
        return userRepository.save(user);
    }

    // updateUser(Long, User) is undefined for the type UserService
    public User updateUser(Long id, User user) {
        if (!userRepository.existsById(id)) {
            return null; // or throw an exception
        }

        user.setId(id);
        user.setPasswordhash(passwordEncoder.encode(user.getPasswordhash()));
        return userRepository.save(user);
    }

    // deleteUser(Long) is undefined for the type UserService
    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return false; // or throw an exception
        }
        userRepository.deleteById(id);
        return true;
    }

}
