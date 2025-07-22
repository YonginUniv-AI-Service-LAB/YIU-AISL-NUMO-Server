package com.aisl.shop.exception.auth;

/**
 * 이메일 인증 코드 검증 실패 예외
 */
public class EmailVerificationFailedException extends RuntimeException {
    public EmailVerificationFailedException(String message) {
        super(message);
    }
}
