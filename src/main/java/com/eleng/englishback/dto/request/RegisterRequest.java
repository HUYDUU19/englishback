package com.eleng.englishback.dto.request;
import lombok.*;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String avatarUrl;
}
