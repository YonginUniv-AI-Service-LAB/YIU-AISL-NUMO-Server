package com.aisl.shop.service.auth;

import com.aisl.shop.service.common.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

    private final Map<String, String> codeStorage = new HashMap<>();
    private final Map<String, Boolean> verifiedEmails = new HashMap<>();
    private final MailService mailService; // ✨ 메일 발송 서비스 주입

    public void sendVerificationCode(String email) {
        String code = generateCode();
        codeStorage.put(email, code);

        String subject = "[쇼핑몰] 이메일 인증코드 안내";
        String content = "인증코드: " + code;

        mailService.send(email, subject, content); // ✉️ 실제 발송
    }

    public void verifyCode(String email, String code) {
        if (!code.equals(codeStorage.get(email))) {
            throw new RuntimeException("인증코드가 일치하지 않습니다.");
        }

        verifiedEmails.put(email, true);
        codeStorage.remove(email);
    }

    public boolean isVerified(String email) {
        return verifiedEmails.getOrDefault(email, false);
    }

    public void clearVerified(String email) {
        verifiedEmails.remove(email);
    }

    private String generateCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }
}
