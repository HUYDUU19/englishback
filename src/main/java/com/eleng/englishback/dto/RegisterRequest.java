package com.eleng.englishback.dto;
import lombok.Data;


@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;
}
