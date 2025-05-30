package com.eleng.englishback.controller;

import com.eleng.englishback.domain.User;
import com.eleng.englishback.dto.response.UserProgressDTO;
import com.eleng.englishback.repository.UserRepository;
import com.eleng.englishback.repository.UserProgressRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserProgressController {

    private final UserRepository userRepository;
    private final UserProgressRepository userProgressRepository;

    @GetMapping("/progress")
    public ResponseEntity<List<UserProgressDTO>> getUserProgress(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null)
            return ResponseEntity.notFound().build();

        List<UserProgressDTO> progressList = userProgressRepository.findByUser(user).stream()
                .map(p -> new UserProgressDTO(
                        p.getLesson().getId(),
                        p.getLesson().getTitle(),
                        p.getScore(),
                        p.getCompleted()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(progressList);
    }
}
