package com.aisl.shop.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void send(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to); // 사용자가 입력한 이메일
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("alsehd2414@gmail.com"); // 여기 실제 이메일로 수정

        mailSender.send(message);
    }
}
