package com.aisl.shop.jwt;

import com.aisl.shop.config.CustomUserDetails;
import com.aisl.shop.config.CustomUserDetailsService;
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
            String token = extractTokenFromHeader(request); // ✅ 변경된 부분

            if (token != null && jwtProvider.isValidToken(token)) {
                Long userId = jwtProvider.getUserId(token);
                if (userId != null) {
                    CustomUserDetails userDetails =
                            (CustomUserDetails) userDetailsService.loadUserByUsername(String.valueOf(userId));

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

        } catch (Exception e) {
            log.warn("[JwtAuthenticationFilter] JWT 인증 실패: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // "Bearer " 이후부터 자름
        }
        return null;
    }
}
