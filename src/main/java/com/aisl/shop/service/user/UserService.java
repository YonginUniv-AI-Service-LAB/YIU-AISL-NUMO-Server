package com.aisl.shop.service.user;

import com.aisl.shop.dto.request.auth.SignupRequest;
import com.aisl.shop.dto.request.user.UpdateUserRequest;
import com.aisl.shop.dto.response.user.UserResponse;
import com.aisl.shop.entity.User;
import com.aisl.shop.enums.Provider;
import com.aisl.shop.enums.Role;
import com.aisl.shop.exception.user.DuplicateResourceException;
import com.aisl.shop.exception.user.UserNotFoundException;
import com.aisl.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ✅ 회원가입
    @Transactional
    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("이미 사용 중인 이메일입니다.");
        }

        if (userRepository.existsByNickname(request.getNickname())) {
            throw new DuplicateResourceException("이미 사용 중인 닉네임입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .nickname(request.getNickname())
                .role(Role.USER)
                .provider(Provider.LOCAL)
                .build();

        userRepository.save(user);
    }

    // ✅ 내 정보 조회
    @Transactional(readOnly = true)
    public UserResponse getMyInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        return UserResponse.from(user);
    }

    // ✅ 내 정보 수정
    @Transactional
    public void updateMyInfo(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        if (!user.getNickname().equals(request.getNickname()) &&
                userRepository.existsByNickname(request.getNickname())) {
            throw new DuplicateResourceException("이미 사용 중인 닉네임입니다.");
        }

        user.setName(request.getName());
        user.setNickname(request.getNickname());
        // save() 생략 가능 (JPA Dirty Checking)
    }
}
