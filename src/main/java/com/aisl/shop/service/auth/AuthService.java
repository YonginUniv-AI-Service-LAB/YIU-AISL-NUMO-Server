package com.aisl.shop.service.auth;

import com.aisl.shop.dto.request.auth.LoginRequest;
import com.aisl.shop.dto.response.auth.TokenResponse;
import com.aisl.shop.entity.User;
import com.aisl.shop.jwt.JwtProvider;
import com.aisl.shop.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
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
                .orElseThrow(() -> new BadCredentialsException("이메일이 존재하지 않거나 비밀번호가 일치하지 않습니다"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("이메일이 존재하지 않거나 비밀번호가 일치하지 않습니다");
        }

        Long userId = user.getId();
        String role = user.getRole().name(); // ✅ 역할 정보 가져오기

        String accessToken = jwtProvider.generateAccessToken(userId, role); // ✅ 역할 포함한 토큰 생성
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());

        log.info("[로그인 성공] 사용자 ID: {}, 이메일: {}, 역할: {}", userId, user.getEmail(), role);

        // RefreshToken 저장
        refreshTokenStore.put(userId, refreshToken);

        return new TokenResponse(accessToken, refreshToken);
    }

    public boolean validateRefreshToken(Long userId, String refreshToken) {
        return refreshToken.equals(refreshTokenStore.get(userId));
    }

    public TokenResponse reissueAccessToken(Long userId, String refreshToken) {
        if (!validateRefreshToken(userId, refreshToken)) {
            throw new RuntimeException("RefreshToken이 유효하지 않습니다");
        }

        // 유저의 role도 다시 포함해야 함
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        String newAccessToken = jwtProvider.generateAccessToken(userId, user.getRole().name());

        return new TokenResponse(newAccessToken, refreshToken); // 기존 RefreshToken 재사용
    }
}
