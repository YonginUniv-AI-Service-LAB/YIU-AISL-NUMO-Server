package com.aisl.shop.service.auth;

import com.aisl.shop.dto.request.auth.SignupRequest;
import com.aisl.shop.entity.User;
import com.aisl.shop.enums.Provider;
import com.aisl.shop.enums.Role;
import com.aisl.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signupAsAdmin(SignupRequest request) {
        if (!request.getEmail().equalsIgnoreCase("admin@aisl.shop")) {
            throw new IllegalArgumentException("관리자 계정은 허용된 이메일로만 생성할 수 있습니다.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .nickname(request.getNickname())
                .role(Role.ADMIN)
                .provider(Provider.LOCAL)
                .build();

        userRepository.save(user);
    }
}
