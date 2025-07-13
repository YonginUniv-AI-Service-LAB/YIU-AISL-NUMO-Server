package com.aisl.shop.dto.response.user;

import com.aisl.shop.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserResponse {

    private String email;
    private String name;
    private String nickname;
    private String role;

    // User → UserResponse 변환 메서드
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .role(user.getRole().name()) // USER / ADMIN
                .build();
    }
}
