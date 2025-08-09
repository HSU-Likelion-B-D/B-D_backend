package com.likelion.bd.global.filter;

import com.likelion.bd.global.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
// OncePerRequestFilter: 매 요청마다 한 번 실행되는 JWT 필터
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    // 요청이 들어올 때마다 JWT 토큰을 검증함
    // 유효한 경우 인증 정보를 SecurityContext 에 저장
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. extractToken: Authorization 헤더에서 JWT 토큰 추출
        String token = jwtTokenProvider.resolveToken(request);

        // 2. validateToken: 토큰 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 3. getAuthentication: 토큰으로 사용자 인증 객체 생성!!
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // 현재 요청의 SecurityContext 에 인증 정보 넣기
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}
