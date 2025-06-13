package com.eleng.englishback.security;

import com.eleng.englishback.repository.UserRepository;
import com.eleng.englishback.service.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    // Phương thức lọc xác thực JWT được gọi cho mỗi request
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException { // Lấy header xác thực từ request
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Kiểm tra nếu header không tồn tại hoặc không đúng định dạng "Bearer {token}"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Trích xuất token JWT (bỏ "Bearer " ở đầu)
        final String token = authHeader.substring(7); // Lấy tên người dùng từ token
        final String username = jwtService.extractUsername(token);
        System.out.println("🔍 JWT Filter: Extracted username = " + username);

        // Kiểm tra nếu username hợp lệ và chưa có xác thực trong SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("🔍 JWT Filter: Processing authentication for username = " + username);
            userRepository.findByUsername(username).ifPresent(user -> {
                System.out.println("🔍 JWT Filter: Found user = " + user.getUsername() + ", role = " + user.getRole());

                // Sử dụng phương thức isTokenValid từ JwtService
                if (!jwtService.isTokenValid(token)) {
                    System.out.println("❌ JWT Filter: Token is invalid");
                    return;
                }

                String roleName = "ROLE_" + user.getRole().name();
                System.out.println("✅ JWT Filter: Setting role = " + roleName);
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), null, List.of(authority));
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("✅ JWT Filter: Authentication set successfully for " + username);
            });
        } // Tiếp tục chuỗi lọc để xử lý các bộ lọc tiếp theo
        filterChain.doFilter(request, response);
    }
}
