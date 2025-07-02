package com.aisl.shop.handler.auth;

import com.aisl.shop.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider; // ✅ 수정됨

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        // JWT 생성
        String token = jwtProvider.generateToken(email); // ✅ 메서드명도 맞게 사용

        // 로그 출력
        log.info("OAuth2 로그인 성공 - email: {}", email);
        log.info("JWT 발급 완료 - token: {}", token);

        // 1. 쿠키로 전달
        Cookie jwtCookie = new Cookie("accessToken", token);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(false); // 보안 강화 시 true 권장
        jwtCookie.setMaxAge(60 * 60); // 1시간
        response.addCookie(jwtCookie);

        // 2. 리다이렉트로 전달 (SPA 대응)
        response.sendRedirect("/auth/oauth/success?token=" + token);
    }
}
