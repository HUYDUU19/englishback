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
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                    corsConfiguration.setAllowedOriginPatterns(java.util.List.of("*"));
                    corsConfiguration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(java.util.List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - no authentication required
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/files/**").permitAll()
                        .requestMatchers("/audio/**").permitAll()
                        .requestMatchers("/image/**").permitAll()
                        .requestMatchers("/static/**").permitAll() // Admin only endpoints - explicitly enforce ADMIN
                                                                   // role for all admin routes
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/debug/**").hasRole("ADMIN") // Explicit protection for debug
                                                                                 // endpoints

                        // User profile and dashboard endpoints - authenticated users only
                        .requestMatchers("/api/users/profile").authenticated()
                        .requestMatchers("/api/users/dashboard").authenticated()
                        .requestMatchers("/api/users/stats").authenticated()
                        .requestMatchers("/api/users/**").authenticated()

                        // Learning content endpoints - authenticated users only
                        .requestMatchers("/api/lessons/**").authenticated()
                        .requestMatchers("/api/exercises/**").authenticated()
                        .requestMatchers("/api/courses/**").authenticated()

                        // Interactive features - authenticated users only
                        .requestMatchers("/api/flashcards/**").authenticated()
                        .requestMatchers("/api/quiz/**").authenticated()
                        .requestMatchers("/api/quizzes/**").authenticated()

                        // Achievement and progress endpoints - authenticated users only
                        .requestMatchers("/api/certificates/**").authenticated()
                        .requestMatchers("/api/progress/**").authenticated()

                        // Feedback endpoint - allow all authenticated users
                        .requestMatchers("/api/feedback/**").authenticated()

                        // Any other request needs authentication
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Thêm cấu hình sau để tắt security default password

    // public static void main(String[] args) {
    // System.out.println(new BCryptPasswordEncoder().encode("123456")); // Mã hóa
    // mật khẩu mặc định
    // }

}
