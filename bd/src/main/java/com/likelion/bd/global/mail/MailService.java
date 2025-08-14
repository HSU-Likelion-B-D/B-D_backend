package com.likelion.bd.global.mail;

import com.likelion.bd.global.exception.CustomException;
import com.likelion.bd.global.response.code.mail.MailErrorResponseCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender;
    private final RedisTemplate<String, String> redisTemplate; // Redis 사용을 위해 주입

    @Value("${auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    public void sendEmail(String toEmail,String title, String content) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toEmail);
        helper.setSubject(title);
        helper.setText(content, true); // true를 설정해서 HTML을 사용 가능

        try {
            emailSender.send(message);
        } catch (RuntimeException e) {
            throw new CustomException(MailErrorResponseCode.MAIL_SEND_FAILED_500);
        }
    }

    // 인증번호 전송
    @Transactional
    public void sendAuthCode(String toEmail) {
        // 인증번호 생성
        String authCode = createAuthCode();

        // 이메일 제목 및 내용 설정
        String subject = "[BD] 회원가입 이메일 인증번호 입니다.";
        String htmlContent =
                "<div style='font-family: Arial, sans-serif; background-color:#f9f9f9; padding:30px;'>" +
                        "<div style='max-width:500px; margin:auto; background:white; padding:30px; border-radius:8px; box-shadow:0 2px 8px rgba(0,0,0,0.1);'>" +
                        "<h2 style='color:#333; text-align:center; margin-bottom:20px;'>이메일 인증 안내</h2>" +
                        "<p style='font-size:15px; color:#555; text-align:center;'>아래 인증번호를 입력해주세요.</p>" +
                        "<div style='margin:30px 0; text-align:center;'>" +
                        "<span style='font-size:28px; font-weight:bold; letter-spacing:4px; color:#4CAF50;'>" + authCode + "</span>" +
                        "</div>" +
                        "<p style='font-size:13px; color:#888; text-align:center;'>본 메일은 발신전용이며, 잘못 수신하셨다면 무시하셔도 됩니다.</p>" +
                        "</div>" +
                        "</div>";

        // 이메일 전송
        try {
            sendEmail(toEmail, subject, htmlContent); // 예외가 발생할 수 있는 코드를 try로 감쌈
        } catch (MessagingException e) {
            throw new CustomException(MailErrorResponseCode.MAIL_SEND_FAILED_500);
        }

        // Redis에 인증번호 저장 (키는 메일, 값은 인증번호, 유효시간: 5분)
        redisTemplate.opsForValue().set(toEmail, authCode, Duration.ofMillis(authCodeExpirationMillis));
    }

    // 인증번호 생성
    private String createAuthCode() {
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            int code = random.nextInt(900000) + 100000; // 100000 ~ 999999
            return String.valueOf(code);
        } catch (NoSuchAlgorithmException e) {
            // getInstanceStrong()에서 발생할 수 있는 예외. 발생 가능성 매우 낮음.
            throw new CustomException(MailErrorResponseCode.AUTH_CODE_GENERATION_FAILED_500);
        }
    }

    // 인증번호 검증
    public void verifyCode(String email, String authCode) {
        // Redis에서 이메일을 Key로 저장된 인증번호를 꺼냅니다.
        String storedCode = redisTemplate.opsForValue().get(email);

        // Redis에 인증번호가 존재하지 않거나, 이미 만료된 경우
        if (storedCode == null) {
            throw new CustomException(MailErrorResponseCode.MAIL_AUTH_CODE_NOT_FOUND_404);
        }

        // 입력된 번호와 다르면 예외
        if (!storedCode.equals(authCode)) {
            throw new CustomException(MailErrorResponseCode.MAIL_INVALID_AUTH_CODE_400);
        }

        // 검증 성공 시, Redis에서 인증번호를 삭제
        redisTemplate.delete(email);
    }
}
