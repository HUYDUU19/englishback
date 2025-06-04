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
        final String token = authHeader.substring(7);
        // Lấy tên người dùng từ token
        final String username = jwtService.extractUsername(token); // Kiểm tra nếu username hợp lệ và chưa có xác thực
                                                                   // trong SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            userRepository.findByUsername(username).ifPresent(user -> {
                // Sử dụng phương thức isTokenValid từ JwtService
                // Phương thức này chỉ cần token vì nó sẽ tự trích xuất tên người dùng và kiểm
                // tra thời gian hết hạn
                if (!jwtService.isTokenValid(token)) {
                    return;
                }

                String roleName = "ROLE_" + user.getRole().name(); // Tạo tên vai trò: ROLE_USER hoặc ROLE_ADMIN
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName); // Tạo đối tượng quyền hạn

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), null, List.of(authority)); // Tạo token xác thực với tên người dùng và quyền
                                                                       // hạn
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Thêm thông tin chi
                                                                                                  // tiết từ request
                SecurityContextHolder.getContext().setAuthentication(authToken); // Thiết lập thông tin xác thực vào
                                                                                 // Security Context
            });
        } // Tiếp tục chuỗi lọc để xử lý các bộ lọc tiếp theo
        filterChain.doFilter(request, response);
    }
}
