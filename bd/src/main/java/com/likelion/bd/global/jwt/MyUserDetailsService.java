package com.likelion.bd.global.jwt;

import com.likelion.bd.domain.user.entity.User;
import com.likelion.bd.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        // DB 에서 사용자 정보 조회
        User user = userRepository.getById(Long.parseLong(userId));
        // 조회한 User 객체 전체를 UserPrincipal 로 감싸서 반환
        return new UserPrincipal(user);
    }
}
