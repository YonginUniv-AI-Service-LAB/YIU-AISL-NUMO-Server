package com.aisl.shop.controller.auth;

import com.aisl.shop.dto.request.auth.PasswordResetRequest;
import com.aisl.shop.service.auth.PasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    @PatchMapping("/password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid PasswordResetRequest request) {
        passwordService.resetPassword(request);
        return ResponseEntity.ok().build();
    }
}
