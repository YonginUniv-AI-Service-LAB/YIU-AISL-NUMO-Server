package com.aisl.shop.config;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final boolean isAdmin;

    public CustomUserDetails(Long id, String email, String password, boolean isAdmin) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    /**
     * ✅ ROLE 설정 (ROLE_USER 또는 ROLE_ADMIN)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = isAdmin ? "ROLE_ADMIN" : "ROLE_USER";
        return List.of(() -> role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override public String getPassword() { return password; }

    @Override public boolean isAccountNonExpired() { return true; }

    @Override public boolean isAccountNonLocked() { return true; }

    @Override public boolean isCredentialsNonExpired() { return true; }

    @Override public boolean isEnabled() { return true; }
}
