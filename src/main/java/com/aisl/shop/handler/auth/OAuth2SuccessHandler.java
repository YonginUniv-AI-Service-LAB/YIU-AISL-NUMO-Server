package com.aisl.shop.handler.auth;

import com.aisl.shop.jwt.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // OAuth2User에서 email 추출
        String email = authentication.getName(); // 기본적으로 OAuth2User의 name이 email임

        // ✅ RefreshToken 발급 (generateRefreshToken 사용)
        String token = jwtProvider.generateRefreshToken(email);

        // ✅ 토큰을 httpOnly 쿠키로 저장 (보안용)
        Cookie cookie = new Cookie("refreshToken", URLEncoder.encode(token, StandardCharsets.UTF_8));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 14); // 14일

        response.addCookie(cookie);

        // ✅ 리다이렉트 or 응답 설정
        response.sendRedirect("/login-success"); // 프론트에서 처리할 경로로 수정 가능
    }
}
