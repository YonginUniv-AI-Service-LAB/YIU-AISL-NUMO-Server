package com.aisl.shop.controller.auth;

import com.aisl.shop.dto.request.auth.GoogleLoginRequest;
import com.aisl.shop.dto.response.auth.AuthResponse;
import com.aisl.shop.entity.User;
import com.aisl.shop.jwt.JwtProvider;
import com.aisl.shop.service.auth.GoogleOAuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final GoogleOAuthService googleOAuthService;
    private final JwtProvider jwtProvider;

    @PostMapping("/google")
    @Operation(summary = "구글 로그인", description = "구글 id_token을 이용하여 로그인 처리 후 JWT 발급")
    public AuthResponse loginWithGoogle(@RequestBody GoogleLoginRequest request) {
        User user = googleOAuthService.loginWithGoogle(request.getIdToken());

        String jwt = jwtProvider.generateAccessToken(user.getId());

        return AuthResponse.builder()
                .accessToken(jwt)
                .user(AuthResponse.UserInfo.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(user.getName())
                        .build())
                .build();
    }
}
