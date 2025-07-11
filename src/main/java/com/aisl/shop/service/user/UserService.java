package com.aisl.shop.service.user;

import com.aisl.shop.dto.request.user.SignupRequest;
import com.aisl.shop.dto.request.user.UpdateUserRequest;
import com.aisl.shop.dto.response.user.UserResponse;
import com.aisl.shop.entity.User;
import com.aisl.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ✅ 회원가입 (phone 제거됨)
    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .nickname(request.getNickname())
                .role(User.Role.USER)
                .provider(User.Provider.LOCAL)
                .build();

        userRepository.save(user);
    }

    // ✅ 내 정보 조회
    public UserResponse getMyInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return UserResponse.from(user);
    }

    // ✅ 내 정보 수정 (phone은 남겨둠 — 필요 시 제거 가능)
    public void updateMyInfo(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.setName(request.getName());
        user.setNickname(request.getNickname());
      

        userRepository.save(user);
    }
}
