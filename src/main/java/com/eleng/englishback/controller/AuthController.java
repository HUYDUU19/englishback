package com.eleng.englishback.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.eleng.englishback.repository.UserRepository;
import com.eleng.englishback.service.JwtService;

import lombok.*;
import com.eleng.englishback.domain.User;
import com.eleng.englishback.domain.Role;
import com.eleng.englishback.dto.request.LoginRequest;
import com.eleng.englishback.dto.request.RegisterRequest;
import com.eleng.englishback.dto.response.AuthResponse;

import org.springframework.http.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    //
    // Add your authentication methods here
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setAvatarUrl(request.getAvatarUrl());

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("Raw: " + request.getPassword());
        System.out.println("From DB: " + user.getPasswordHash());
        System.out.println("Match? " + passwordEncoder.matches(request.getPassword(), user.getPasswordHash()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.badRequest().body("Invalid password");
        }
        String token = jwtService.generateToken(user);

        // Tạo user object để trả về frontend (loại bỏ password)
        User userResponse = new User();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setFullName(user.getFullName());
        userResponse.setAvatarUrl(user.getAvatarUrl());
        userResponse.setRole(user.getRole());
        userResponse.setActive(user.isActive());
        userResponse.setCreatedAt(user.getCreatedAt());
        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRole().name(), userResponse));
    }

    // Endpoint để tạo admin user (chỉ dành cho development hoặc super admin)
    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User admin = new User();
        admin.setUsername(request.getUsername());
        admin.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        admin.setEmail(request.getEmail());
        admin.setFullName(request.getFullName());
        admin.setAvatarUrl(request.getAvatarUrl());
        admin.setRole(Role.ADMIN); // Đặt role là ADMIN

        userRepository.save(admin);
        return ResponseEntity.ok("Admin user created successfully");
    }

    // Endpoint để tạo admin mặc định cho hệ thống (chỉ dùng một lần)
    @PostMapping("/create-default-admin")
    public ResponseEntity<?> createDefaultAdmin() {
        // Kiểm tra xem đã có admin nào chưa
        if (userRepository.existsByRole(Role.ADMIN)) {
            return ResponseEntity.badRequest().body("Admin user already exists");
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setPasswordHash(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@leenglish.com");
        admin.setFullName("System Administrator");
        admin.setRole(Role.ADMIN);

        userRepository.save(admin);

        return ResponseEntity.ok("Default admin created successfully. Username: admin, Password: admin123");
    }

}
