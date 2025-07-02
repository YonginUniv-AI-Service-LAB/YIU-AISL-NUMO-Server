package com.aisl.shop.service.auth;

import com.aisl.shop.dto.request.auth.LoginRequest;
import com.aisl.shop.dto.response.auth.TokenResponse;
import com.aisl.shop.entity.User;
import com.aisl.shop.jwt.JwtProvider;
import com.aisl.shop.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // 간단한 RefreshToken 저장소 (→ 추후 Redis나 DB로 대체 가능)
    private Map<Long, String> refreshTokenStore;

    @PostConstruct
    public void init() {
        refreshTokenStore = new HashMap<>();
    }

    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("이메일이 존재하지 않습니다"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

        Long userId = user.getId();
        String accessToken = jwtProvider.generateAccessToken(userId);
        String refreshToken = jwtProvider.generateRefreshToken(String.valueOf(userId)); // ✅ 수정

        // RefreshToken 저장
        refreshTokenStore.put(userId, refreshToken);

        return new TokenResponse(accessToken, refreshToken);
    }

    public boolean validateRefreshToken(Long userId, String refreshToken) {
        return refreshToken.equals(refreshTokenStore.get(userId));
    }

    // 필요 시 RefreshToken으로 새로운 AccessToken 발급
    public TokenResponse reissueAccessToken(Long userId, String refreshToken) {
        if (!validateRefreshToken(userId, refreshToken)) {
            throw new RuntimeException("RefreshToken이 유효하지 않습니다");
        }

        String newAccessToken = jwtProvider.generateAccessToken(userId);
        return new TokenResponse(newAccessToken, refreshToken); // 기존 RefreshToken 재사용
    }
}
