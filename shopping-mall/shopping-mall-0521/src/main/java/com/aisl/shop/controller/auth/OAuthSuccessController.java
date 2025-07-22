package com.aisl.shop.controller.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthSuccessController {

    @GetMapping("/auth/oauth/success")
    public String success(@RequestParam String token) {
        return "✅ 소셜 로그인 성공!<br/>JWT 토큰:<br/><code>" + token + "</code>";
    }
}
