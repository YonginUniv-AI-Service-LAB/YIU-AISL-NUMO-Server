package com.aisl.shop.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String token = extractTokenFromCookies(request);

            if (token != null && jwtProvider.isValidToken(token)) {
                Long userId = jwtProvider.getUserId(token);
                if (userId != null) {
                    request.setAttribute("userId", userId);
                }
            }

        } catch (Exception e) {
            // 유효하지 않은 토큰이거나 예외 발생 시 무시하고 로그만 출력
            System.out.println("[JwtAuthenticationFilter] JWT 검증 실패: " + e.getMessage());
        }

        filterChain.doFilter(request, response); // 필수 호출
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("access_token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
