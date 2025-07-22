package com.aisl.shop.controller.user;

import com.aisl.shop.dto.request.user.PasswordResetRequest;
import com.aisl.shop.service.user.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    @PatchMapping("/password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
        passwordService.resetPassword(request);
        return ResponseEntity.ok().build();
    }
}
