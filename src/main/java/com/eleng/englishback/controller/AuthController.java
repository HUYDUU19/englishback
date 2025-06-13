package com.eleng.englishback.controller;

import com.eleng.englishback.domain.User;
import com.eleng.englishback.domain.Role;
import com.eleng.englishback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> registrationData) {
        Map<String, Object> response = new HashMap<>();

        String username = registrationData.get("username");
        String email = registrationData.get("email");
        String password = registrationData.get("password");

        // Check if user already exists
        if (userRepository.findByUsername(username).isPresent()) {
            response.put("success", false);
            response.put("message", "Username already exists");
            return ResponseEntity.badRequest().body(response);
        }

        if (userRepository.findByEmail(email).isPresent()) {
            response.put("success", false);
            response.put("message", "Email already exists");
            return ResponseEntity.badRequest().body(response);
        }

        // Create new user
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordhash(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        user.setIsPremium(false);
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        response.put("success", true);
        response.put("message", "User registered successfully");
        response.put("user", createUserResponse(savedUser));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        Map<String, Object> response = new HashMap<>();

        String username = loginData.get("username");
        String password = loginData.get("password");

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (!user.getIsActive()) {
                response.put("success", false);
                response.put("message", "Account is deactivated");
                return ResponseEntity.badRequest().body(response);
            }

            if (passwordEncoder.matches(password, user.getPasswordhash())) {
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("user", createUserResponse(user));
                return ResponseEntity.ok(response);
            }
        }

        response.put("success", false);
        response.put("message", "Invalid username or password");
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/login/email")
    public ResponseEntity<Map<String, Object>> loginWithEmail(@RequestBody Map<String, String> loginData) {
        Map<String, Object> response = new HashMap<>();

        String email = loginData.get("email");
        String password = loginData.get("password");

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (!user.getIsActive()) {
                response.put("success", false);
                response.put("message", "Account is deactivated");
                return ResponseEntity.badRequest().body(response);
            }

            if (passwordEncoder.matches(password, user.getPasswordhash())) {
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("user", createUserResponse(user));
                return ResponseEntity.ok(response);
            }
        }

        response.put("success", false);
        response.put("message", "Invalid email or password");
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(@RequestBody Map<String, String> passwordData) {
        Map<String, Object> response = new HashMap<>();

        String username = passwordData.get("username");
        String currentPassword = passwordData.get("currentPassword");
        String newPassword = passwordData.get("newPassword");

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(currentPassword, user.getPasswordhash())) {
                user.setPasswordhash(passwordEncoder.encode(newPassword));
                userRepository.save(user);

                response.put("success", true);
                response.put("message", "Password changed successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Current password is incorrect");
                return ResponseEntity.badRequest().body(response);
            }
        }

        response.put("success", false);
        response.put("message", "User not found");
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/verify/{username}")
    public ResponseEntity<Map<String, Object>> verifyUser(@PathVariable String username) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            response.put("success", true);
            response.put("user", createUserResponse(user));
            return ResponseEntity.ok(response);
        }

        response.put("success", false);
        response.put("message", "User not found");
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/upgrade-premium/{userId}")
    public ResponseEntity<Map<String, Object>> upgradeToPremium(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setIsPremium(true);
            User updatedUser = userRepository.save(user);

            response.put("success", true);
            response.put("message", "User upgraded to premium successfully");
            response.put("user", createUserResponse(updatedUser));
            return ResponseEntity.ok(response);
        }

        response.put("success", false);
        response.put("message", "User not found");
        return ResponseEntity.badRequest().body(response);
    }

    private Map<String, Object> createUserResponse(User user) {
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", user.getId());
        userResponse.put("username", user.getUsername());
        userResponse.put("email", user.getEmail());
        userResponse.put("fullName", user.getFullName());
        userResponse.put("role", user.getRole().toString());
        userResponse.put("isPremium", user.getIsPremium());
        userResponse.put("isActive", user.getIsActive());
        userResponse.put("createdAt", user.getCreatedAt());
        userResponse.put("premiumExpiresAt", user.getPremiumExpiresAt());
        return userResponse;
    }
}
