package com.aisl.shop.service.auth;

import com.aisl.shop.entity.User;
import com.aisl.shop.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final UserRepository userRepository;

    // ✅ 실제 Google Cloud Console에서 발급받은 클라이언트 ID로 교체하세요
    private static final String CLIENT_ID = "YOUR_GOOGLE_CLIENT_ID.apps.googleusercontent.com";

    public User loginWithGoogle(String idTokenString) {
        try {
            // 구글 기본 전송/파서 도구
            HttpTransport transport = Utils.getDefaultTransport();
            JsonFactory jsonFactory = Utils.getDefaultJsonFactory();

            // ID 토큰 검증기 생성
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(Collections.singletonList(CLIENT_ID)) // 여기 반드시 일치해야 함
                    .build();

            // 토큰 검증
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new RuntimeException("❌ 유효하지 않은 Google ID Token입니다.");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            // 사용자 정보 추출
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String sub = payload.getSubject(); // 구글 고유 사용자 ID

            // DB 조회 및 사용자 자동 등록
            Optional<User> userOpt = userRepository.findByEmail(email);

            return userOpt.orElseGet(() -> {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName(name != null ? name : "GoogleUser");
                newUser.setProvider(User.Provider.GOOGLE);
                newUser.setProviderId(sub);
                newUser.setRole(User.Role.USER);
                newUser.setNickname("google_" + sub.substring(0, 6));
                newUser.setPhone("010-0000-0000");
                return userRepository.save(newUser);
            });

        } catch (Exception e) {
            // 로그 찍고 나중에 더 세분화해도 좋습니다.
            throw new RuntimeException("Google 로그인 처리 중 오류 발생", e);
        }
    }
}
