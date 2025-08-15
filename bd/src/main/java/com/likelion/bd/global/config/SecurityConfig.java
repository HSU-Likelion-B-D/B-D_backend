package com.likelion.bd.global.config;

import com.likelion.bd.global.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    // AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 비활성화
        http
                .csrf((auth) -> auth.disable());

        // From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        // http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        // 경로별 인가 작업
        http
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/user/signup", "/user/profile", "/user/signin",
                                "/user/check-email", "/user/check-nickname",
                                "/user/sendcode", "/user/verifycode",
                                "user/pwchange").permitAll()
                        // 자영업자
                        .requestMatchers(HttpMethod.POST,"/api/businessman/workplaces").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/businessman/workplaces").hasRole("BUSINESS")
                        // 인플루언서
                        .requestMatchers(HttpMethod.POST,"/api/influencer/activities").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/influencer/activities").hasRole("INFLUENCER")
                        .requestMatchers("/api/influencer/mypage").hasRole("INFLUENCER")
                        .anyRequest().authenticated()  // 그 외 요청은 전부 토큰 필요
                );

        // JWTFilter 등록
        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);


        // 세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
