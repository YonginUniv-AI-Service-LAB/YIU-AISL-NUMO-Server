package com.aisl.shop.controller.user;

import com.aisl.shop.dto.request.user.SignupRequest;
import com.aisl.shop.dto.request.user.UpdateUserRequest;
import com.aisl.shop.dto.response.user.UserResponse;
import com.aisl.shop.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ✅ 회원가입
    @PostMapping
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // ✅ 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(userService.getMyInfo(userId));
    }

    // ✅ 내 정보 수정
    @PatchMapping("/me")
    public ResponseEntity<?> updateMyInfo(
            HttpServletRequest request,
            @RequestBody UpdateUserRequest updateRequest
    ) {
        Long userId = (Long) request.getAttribute("userId");
        userService.updateMyInfo(userId, updateRequest);
        return ResponseEntity.ok().build();
    }
}
