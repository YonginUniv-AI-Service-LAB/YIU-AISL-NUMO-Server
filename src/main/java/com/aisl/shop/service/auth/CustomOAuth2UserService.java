package com.aisl.shop.service.auth;

import com.aisl.shop.entity.User;
import com.aisl.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        // 1. 구글로부터 유저 정보 받아오기
        OAuth2User oAuth2User = super.loadUser(request);

        // 2. 유저 정보 추출
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String providerId = oAuth2User.getName(); // Google의 고유 ID

        // 3. DB에서 사용자 존재 여부 확인
        User user = userRepository.findByEmail(email).orElseGet(() ->
                userRepository.save(User.builder()
                        .email(email)
                        .name(name != null ? name : "이름없음")
                        .nickname(name != null ? name : "닉네임없음") // ✅ 기본값 지정
                        .phone("000-0000-0000") // ✅ 임시 전화번호
                        .provider(User.Provider.GOOGLE)
                        .providerId(providerId)
                        .role(User.Role.USER)
                        .build()
                )
        );

        // 4. Spring Security 인증 객체로 변환하여 반환
        return new DefaultOAuth2User(
                Collections.singleton(() -> "ROLE_" + user.getRole()),
                oAuth2User.getAttributes(),
                "email"
        );
    }
}
