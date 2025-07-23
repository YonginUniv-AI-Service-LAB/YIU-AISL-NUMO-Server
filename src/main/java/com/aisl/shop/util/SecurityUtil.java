package com.aisl.shop.util;

import com.aisl.shop.config.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("현재 로그인된 사용자가 없습니다.");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            System.out.println("🔍 principal = " + principal);
            System.out.println("🔍 principal class = " + principal.getClass().getName());
            throw new IllegalStateException("인증된 사용자 정보가 올바르지 않습니다.");
        }

        return ((CustomUserDetails) principal).getId();
    }
}
