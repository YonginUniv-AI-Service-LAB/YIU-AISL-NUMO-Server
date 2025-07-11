package com.aisl.shop.controller.user;

import com.aisl.shop.config.CustomUserDetails;
import com.aisl.shop.dto.request.user.UpdateUserRequest;
import com.aisl.shop.dto.response.user.UserResponse;
import com.aisl.shop.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userService.getMyInfo(userDetails.getId()));
    }

    // 내 정보 수정
    @PatchMapping("/me")
    public ResponseEntity<?> updateMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateUserRequest updateRequest
    ) {
        userService.updateMyInfo(userDetails.getId(), updateRequest);
        return ResponseEntity.ok().build();
    }
}
