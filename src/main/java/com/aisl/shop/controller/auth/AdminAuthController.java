package com.aisl.shop.controller.auth;

import com.aisl.shop.dto.request.auth.SignupRequest;
import com.aisl.shop.service.auth.AdminAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signupAdmin(@RequestBody @Valid SignupRequest request) {
        adminAuthService.signupAsAdmin(request);
        return ResponseEntity.ok().build();
    }
}
