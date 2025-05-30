package com.eleng.englishback.controller;

import com.eleng.englishback.domain.UserMembership;
import com.eleng.englishback.repository.UserMembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/user-memberships")
@RequiredArgsConstructor
public class UserMembershipController {
    private final UserMembershipRepository userMembershipRepository;

    @GetMapping
    public ResponseEntity<List<UserMembership>> getAll() {
        return ResponseEntity.ok(userMembershipRepository.findAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserMembership>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userMembershipRepository.findByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserMembership> getById(@PathVariable Long id) {
        return userMembershipRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserMembership> create(@RequestBody UserMembership userMembership) {
        return ResponseEntity.ok(userMembershipRepository.save(userMembership));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserMembership> update(@PathVariable Long id, @RequestBody UserMembership userMembership) {
        return userMembershipRepository.findById(id)
                .map(existing -> {
                    userMembership.setId(id);
                    return ResponseEntity.ok(userMembershipRepository.save(userMembership));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (userMembershipRepository.existsById(id)) {
            userMembershipRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
