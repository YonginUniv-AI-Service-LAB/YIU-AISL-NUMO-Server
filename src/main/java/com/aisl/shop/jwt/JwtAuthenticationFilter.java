package com.aisl.shop.jwt;

import com.aisl.shop.config.CustomUserDetails;
import com.aisl.shop.config.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, CustomUserDetailsService userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String token = extractTokenFromHeader(request);

            if (token != null && jwtProvider.isValidToken(token)) {
                Long userId = jwtProvider.getUserId(token);
                if (userId != null) {
                    CustomUserDetails userDetails = userDetailsService.loadUserById(userId); // ✅ 수정

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("[JwtAuthenticationFilter] 인증 성공 - 사용자 ID: {}", userId);
                }
            }

        } catch (ExpiredJwtException e) {
            String uri = request.getRequestURI();
            if (uri.startsWith("/auth") || uri.startsWith("/users")) {
                log.warn("[JwtAuthenticationFilter] 만료된 토큰이지만 무시 (경로: {}): {}", uri, e.getMessage());
            } else {
                log.warn("[JwtAuthenticationFilter] 만료된 토큰: {}", e.getMessage());
            }

        } catch (Exception e) {
            log.warn("[JwtAuthenticationFilter] JWT 인증 실패: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
