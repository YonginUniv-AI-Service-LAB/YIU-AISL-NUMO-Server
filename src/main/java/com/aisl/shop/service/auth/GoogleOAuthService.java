package com.aisl.shop.service.auth;

import com.aisl.shop.entity.User;
import com.aisl.shop.repository.UserRepository;
import com.aisl.shop.exception.common.ConflictException;
import com.aisl.shop.exception.common.UnauthorizedException;
import com.aisl.shop.exception.auth.GoogleLoginException; // ✅ 추가한 예외
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final UserRepository userRepository;

    @Value("${oauth.google.client-id}")
    private String clientId;

    public User loginWithGoogle(String idTokenString) {
        try {
            HttpTransport transport = Utils.getDefaultTransport();
            JsonFactory jsonFactory = Utils.getDefaultJsonFactory();

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                log.warn("❌ 유효하지 않은 Google ID Token입니다.");
                throw new UnauthorizedException("유효하지 않은 Google ID Token입니다.");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String sub = payload.getSubject();

            log.info("[Google Login] 사용자 이메일: {}, Google sub: {}", email, sub);

            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User existingUser = userOpt.get();

                if (existingUser.getProvider() != User.Provider.GOOGLE) {
                    log.warn("이미 다른 방식으로 가입된 이메일: {}", email);
                    throw new ConflictException("이미 다른 방식(일반 회원가입)으로 가입된 이메일입니다.");
                }

                return existingUser;
            }

            // 신규 사용자 등록
            User newUser = User.builder()
                    .email(email)
                    .name(name != null ? name : "GoogleUser")
                    .provider(User.Provider.GOOGLE)
                    .providerId(sub)
                    .role(User.Role.USER)
                    .nickname("google_" + sub.substring(0, 6))
                    .build();

            return userRepository.save(newUser);

        } catch (UnauthorizedException | ConflictException e) {
            throw e;
        } catch (Exception e) {
            log.error("[Google 로그인 오류]", e);
            throw new GoogleLoginException("Google 로그인 처리 중 오류가 발생했습니다.", e);
        }
    }
}
