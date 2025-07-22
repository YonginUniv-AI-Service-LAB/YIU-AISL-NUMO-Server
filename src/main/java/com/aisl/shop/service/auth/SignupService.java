package com.aisl.shop.service.auth;

import com.aisl.shop.dto.request.auth.SignupRequest;
import com.aisl.shop.entity.User;
import com.aisl.shop.enums.Role;
import com.aisl.shop.enums.Provider;
import com.aisl.shop.exception.auth.EmailAlreadyExistsException;
import com.aisl.shop.exception.auth.EmailNotVerifiedException;
import com.aisl.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailAuthService emailAuthService;

    // 회원가입
    public void signup(SignupRequest request) {
        // 1. 이메일 인증 여부 확인
        if (!emailAuthService.isVerified(request.getEmail())) {
            throw new EmailNotVerifiedException("이메일 인증이 완료되지 않았습니다.");
        }

        // 2. 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("이미 가입된 이메일입니다.");
        }

        // 3. 회원 정보 저장
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .nickname(request.getNickname())
                .role(Role.USER)                // ✅ 수정
                .provider(Provider.LOCAL)       // ✅ 수정
                .build();

        userRepository.save(user);

        // 4. 인증된 이메일 상태 초기화
        emailAuthService.clearVerified(request.getEmail());
    }

    // 이메일 중복 확인 (중복 확인 API에서 사용됨)
    public boolean isEmailDuplicated(String email) {
        return userRepository.existsByEmail(email);
    }
}
