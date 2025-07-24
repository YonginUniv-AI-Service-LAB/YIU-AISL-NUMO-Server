package com.aisl.shop.config;

import com.aisl.shop.jwt.JwtAuthenticationFilter;
import com.aisl.shop.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                        "/admin/banners/**",     // ✅ 먼저 명시: 배너만 모두 접근 허용
                                        "/", "/test", "/category/**",
                                        "/search/**",
                                        "/products/**",
                                        "/auth/token", "/auth/signup", "/auth/check-email", "/auth/reissue", "/auth/**",
                                        "/auth/oauth/google",
                                        "/auth/admin/signup",
                                        "/emails/verification-code", "/emails/verification-code/verify",
                                        "/users/password",
                                        "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
                                        "/images/**", "/upload/**", "/favicon.ico",
                                        "/orders/**", "/wishlist/**", "/reviews/**",
                                        "/habits/**", "/diaries/**", "/cart/**"
                                ).permitAll()

                                .requestMatchers("/admin/**").hasRole("ADMIN") // ❗ 그 외의 /admin 경로는 ADMIN만 허용
                                .anyRequest().authenticated()

                )


                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtProvider, customUserDetailsService),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://44284814efff.ngrok-free.app",
                "http://14.47.205.106:8080"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*", "X-Requested-With", "Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);  // 이 옵션 때문에 allowedOrigins만 허용됨

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
