package com.eleng.englishback.dto.request;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String fullName;
    private String email;
    private String avatarUrl;
}
