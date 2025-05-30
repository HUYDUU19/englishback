package com.eleng.englishback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.eleng.englishback.security.JwtAuthenticationFilter;

import lombok.*;

@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> {
                }) // ✅ Bỏ qua CORS, đã cấu hình trong CorsConfig

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/lessons/**").hasRole("USER") // ✅ Thay đổi từ hasAnyRole
                        .requestMatchers("/api/exercises/**").hasRole("USER") // ✅ Thay đổi từ hasAnyRole
                        .requestMatchers("/api/users/profile").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/flashcard").hasRole("USER")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // ✅ thay thế

                                                                                                       // httpBasicfilter
        return http.build();
    }

    // Thêm cấu hình sau để tắt security default password

    // public static void main(String[] args) {
    // System.out.println(new BCryptPasswordEncoder().encode("123456")); // Mã hóa
    // mật khẩu mặc định
    // }

}
