package com.eleng.englishback.dto.response;

import lombok.*;
import com.eleng.englishback.domain.User;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private String role;
    private User user; // Thêm user object để frontend có thể sử dụng
}
