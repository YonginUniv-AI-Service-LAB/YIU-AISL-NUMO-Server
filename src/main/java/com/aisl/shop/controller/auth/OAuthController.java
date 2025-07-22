package com.aisl.shop.controller.auth;

import com.aisl.shop.dto.request.auth.GoogleLoginRequest;
import com.aisl.shop.dto.response.auth.AuthResponse;
import com.aisl.shop.entity.User;
import com.aisl.shop.jwt.JwtProvider;
import com.aisl.shop.service.auth.GoogleOAuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final GoogleOAuthService googleOAuthService;
    private final JwtProvider jwtProvider;


    @PostMapping("/google")
    @Operation(summary = "구글 로그인", description = "Google ID Token으로 로그인 처리 후 JWT 반환")
    public AuthResponse loginWithGoogle(@RequestBody @Valid GoogleLoginRequest request) {
        // 1. 사용자 인증 및 회원 조회/생성
        User user = googleOAuthService.loginWithGoogle(request.getIdToken());

        // 2. 토큰 발급
        String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getRole().name());
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());

        // 3. JSON 응답으로 access + refresh + user 정보 반환
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(AuthResponse.UserInfo.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(user.getName())
                        .build())
                .build();
    }

}
