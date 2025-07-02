package com.aisl.shop.dto.response.user;

import com.aisl.shop.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    private String email;
    private String name;
    private String nickname;
    private String phone;
    private String role;

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getEmail(),
                user.getName(),
                user.getNickname(),
                user.getPhone(),
                user.getRole().name()  // ✅ enum → String 변환
        );
    }

}
