package com.eleng.englishback.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.eleng.englishback.repository.UserRepository;
import com.eleng.englishback.domain.Role;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    // Endpoint để lấy thống kê cho admin dashboard
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();

        // Đếm tổng số user
        long totalUsers = userRepository.count();

        // Đếm số user thường
        long normalUsers = userRepository.countByRole(Role.USER);

        // Đếm số admin
        long adminUsers = userRepository.countByRole(Role.ADMIN);

        // Mock data cho các thống kê khác
        stats.put("totalUsers", totalUsers);
        stats.put("normalUsers", normalUsers);
        stats.put("adminUsers", adminUsers);
        stats.put("totalLessons", 25); // Mock data
        stats.put("activeTests", 8); // Mock data
        stats.put("pendingCertificates", 12); // Mock data

        return ResponseEntity.ok(stats);
    }

    // Endpoint để lấy danh sách tất cả user (chỉ admin)
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // Endpoint kiểm tra quyền admin
    @GetMapping("/check")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> checkAdminAccess() {
        return ResponseEntity.ok("Admin access confirmed");
    }
}
