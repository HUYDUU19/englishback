package com.eleng.englishback.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProgressDTO {
  private Long lessonId;
  private String lessonTitle;
  private Integer score;
  private Boolean completed;
}
