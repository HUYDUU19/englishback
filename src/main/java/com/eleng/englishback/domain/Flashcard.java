package com.eleng.englishback.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "flashcards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    

    private String word;          // Từ vựng

    private String meaning;       // Nghĩa tiếng Việt

    private String example_sentence;       // Ví dụ

    private String pronunciation; // Phiên âm

    private String audioUrl;      // Đường dẫn file phát âm

    private String imageUrl;      // Hình minh họa (tùy chọn)

    private String level;         // A1, A2, B1, B2, C1, C2
}
