package com.aisl.shop.service.auth;

import com.aisl.shop.dto.request.auth.LoginRequest;
import com.aisl.shop.dto.response.auth.TokenResponse;
import com.aisl.shop.entity.User;
import com.aisl.shop.exception.common.UnauthorizedException;
import com.aisl.shop.exception.auth.InvalidPasswordException;
import com.aisl.shop.exception.auth.UserNotFoundException;
import com.aisl.shop.jwt.JwtProvider;
import com.aisl.shop.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private Map<Long, String> refreshTokenStore;

    @PostConstruct
    public void init() {
        refreshTokenStore = new HashMap<>();
    }

    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("해당 이메일의 사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }

        Long userId = user.getId();
        String role = user.getRole().name();

        String accessToken = jwtProvider.generateAccessToken(userId, role);
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());

        log.info("[로그인 성공] 사용자 ID: {}, 이메일: {}, 역할: {}", userId, user.getEmail(), role);

        refreshTokenStore.put(userId, refreshToken);

        return new TokenResponse(accessToken, refreshToken);
    }

    public boolean validateRefreshToken(Long userId, String refreshToken) {
        return refreshToken.equals(refreshTokenStore.get(userId));
    }

    public TokenResponse reissueAccessToken(Long userId, String refreshToken) {
        if (!validateRefreshToken(userId, refreshToken)) {
            throw new UnauthorizedException("RefreshToken이 유효하지 않습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        String newAccessToken = jwtProvider.generateAccessToken(userId, user.getRole().name());

        return new TokenResponse(newAccessToken, refreshToken);
    }
}
