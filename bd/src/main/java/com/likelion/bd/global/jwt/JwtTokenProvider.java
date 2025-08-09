package com.likelion.bd.global.jwt;

import com.likelion.bd.domain.user.entity.User;
import com.likelion.bd.global.exception.CustomException;
import com.likelion.bd.global.response.code.AuthErrorResponseCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    // 헤더 이름과 접두사를 상수로 정의
    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    private final Key key;
    private final long validityInMilliseconds;
    private final MyUserDetailsService userDetailsService;

    public JwtTokenProvider(
            @Value("${security.jwt.secret-key}") String secretKey,
            @Value("${security.jwt.expiration}") long validityInMilliseconds,
            MyUserDetailsService userDetailsService
    ) {
        // JWT 서명용 키 생성
        // 1. secretKey 가 Base64 인코딩된 경우 → 디코딩하여 원본 바이트 추출
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        // HMAC-SHA256 알고리즘에 사용할 Key 객체 생성
        this.key = Keys.hmacShaKeyFor(keyBytes);

        this.validityInMilliseconds = validityInMilliseconds;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 사용자 ID를 받아 JWT 토큰을 생성합니다.
     * @param user 사용자
     * @return 생성된 JWT 토큰 문자열
     */
    public String createToken(User user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + this.validityInMilliseconds);

        return Jwts.builder()
                .setSubject(String.valueOf(user.getUserId()))  // 토큰의 주체(subject)로 사용자 ID를 문자열로 설정
                .claim("role", user.getRole().name())       // 커스텀 클레임에 사용자 역할 추가
                .setIssuedAt(now)                              // 발급 시각
                .setExpiration(validity)                       // 만료 시각
                .signWith(key)                                 // 준비된 key 를 사용하여 토큰을 서명 (알고리즘은 key 에 이미 포함됨)
                .compact();                                    // 최종적으로 압축된 JWT 문자열을 생성
    }

    /**
     * 토큰 문자열을 파싱하여 클레임(토큰에 담긴 정보)을 추출합니다.
     * 서명 검증에 실패하거나 토큰이 유효하지 않으면 예외가 발생합니다.
     * @param token JWT 토큰 문자열
     * @return 토큰의 클레임 정보
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) // 서명 검증에 사용할 키를 설정합니다.
                .build()
                .parseClaimsJws(token) // 토큰을 파싱하고 서명을 검증합니다.
                // 서명 검증, 만료 시간, 형식 검증, 다양한 기본적인 검사들 수행
                .getBody(); // Payload(Claims) 부분을 반환합니다.
    }

    /**
     * HTTP 요청의 헤더에서 Bearer 토큰을 추출합니다.
     * @param request HttpServletRequest 객체
     * @return 추출된 JWT 문자열 또는 토큰이 없을 경우 null
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(PREFIX)) {
            // StringUtils.hasText(bearerToken) : null, "", "   " (공백만 있는 문자열)까지 모두 false 처리
            // bearerToken.startsWith(PREFIX) : 헤더 값이 지정한 접두어(PREFIX, "Bearer ")로 시작하는지 확인
            return bearerToken.substring(PREFIX.length());
        }
        return null;
    }

    /**
     * 토큰이 유효한지 검증하는 메서드
     * @param token 검증할 JWT 토큰
     * @return 토큰이 유효하면 true, 아니면 false
     */
    public boolean validateToken(String token) {
        try {
            // getClaims 가 내부적으로 토큰을 파싱하고 서명을 검증합니다.
            // 유효하지 않으면 예외가 발생하므로, catch 블록으로 잡힙니다.
            getClaims(token);
            return true;
        } catch (SignatureException e) {
            log.error("유효하지 않은 JWT 서명입니다: {}", e.getMessage());
            throw new CustomException(AuthErrorResponseCode.INVALID_SIGNATURE);
        } catch (MalformedJwtException e) {
            log.error("잘못된 형식의 JWT 토큰입니다: {}", e.getMessage());
            throw new CustomException(AuthErrorResponseCode.MALFORMED_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다: {}", e.getMessage());
            throw new CustomException(AuthErrorResponseCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 JWT 토큰입니다: {}", e.getMessage());
            throw new CustomException(AuthErrorResponseCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error("JWT 클레임 문자열이 비어있습니다: {}", e.getMessage());
            throw new CustomException(AuthErrorResponseCode.ILLEGAL_ARGUMENT);
        }
    }

    /**
     * JWT 토큰을 복호화하여 토큰에 들어있는 정보를 기반으로 Authentication 객체를 반환합니다.
     * 데이터베이스에서 사용자 정보를 조회하여 SecurityContext 에 저장할 완전한 형태의 인증 객체를 생성합니다.
     * @param token JWT 토큰
     * @return 사용자 정보를 담은 Authentication 객체
     */
    public Authentication getAuthentication(String token) {
        // 토큰에서 클레임(정보) 추출
        Claims claims = getClaims(token);

        // 클레임에서 사용자 ID(Subject) 추출
        String userId = claims.getSubject();

        // 사용자 ID를 기반으로 데이터베이스에서 UserDetails 객체 조회
        // 이 UserDetails 는 사용자의 아이디, 패스워드, 권한 등을 담고 있는 Spring Security 의 표준 인터페이스
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

        // UserDetails 객체, 비밀번호(보통 null 처리), 권한 정보를 기반으로 Authentication 객체 생성
        // 이 객체는 Spring Security 가 내부적으로 사용자의 인증 상태를 관리하는 데 사용됩니다.
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
