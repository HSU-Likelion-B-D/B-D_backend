package com.likelion.bd.global.jwt;

import com.likelion.bd.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

    private final Long id;
    private final String role; // "USER" or "ROLE_USER" 형태로 저장

    UserPrincipal(User user) {
        this.id = user.getUserId();
        this.role = user.getRole().name();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = this.role.startsWith("ROLE_") ? this.role : "ROLE_" + this.role;
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        // JWT 인증에서는 비밀번호를 사용하지 않으므로 null 반환
        return null;
    }

    @Override
    public String getUsername() {
        // 사용자를 식별하는 고유한 값 (여기서는 ID)을 문자열로 반환합니다.
        return String.valueOf(id);
    }

    // --- 계정 상태 관련 메서드들 (특별한 로직이 없다면 모두 true 반환) ---

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정이 만료되지 않았음
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정이 잠기지 않았음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명(비밀번호)이 만료되지 않았음
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정이 활성화됨
    }
}
