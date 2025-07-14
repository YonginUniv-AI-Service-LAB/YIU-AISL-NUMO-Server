package com.aisl.shop.controller.auth;

import com.aisl.shop.dto.request.auth.EmailAuthRequest;
import com.aisl.shop.dto.request.auth.EmailVerifyRequest;
import com.aisl.shop.service.auth.EmailAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emails")
public class EmailAuthController {

    private final EmailAuthService emailAuthService;

    @PostMapping("/verification-code")
    public ResponseEntity<?> sendCode(@RequestBody @Valid EmailAuthRequest request) {
        emailAuthService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verification-code/verify")
    public ResponseEntity<?> verifyCode(@RequestBody @Valid EmailVerifyRequest request) {
        emailAuthService.verifyCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok().build();
    }
}

