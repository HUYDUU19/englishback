package com.eleng.englishback.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class UserProfileDTO {
    
     private Long id;
    private String username;
    private String email;
    private String fullName;

    private String avatarUrl;
    
    private String role;


}
