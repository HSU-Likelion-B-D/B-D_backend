package com.likelion.bd.global.response.code.mail;

import com.likelion.bd.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MailErrorResponseCode implements BaseResponseCode {

    MAIL_INVALID_AUTH_CODE_400("MAIL_INVALID_AUTH_CODE_400", 400, "인증번호가 일치하지 않습니다"),
    MAIL_AUTH_CODE_NOT_FOUND_404("MAIL_AUTH_CODE_NOT_FOUND_404", 404, "인증번호가 존재하지 않거나 만료되었습니다."),
    MAIL_SEND_FAILED_500("MAIL_SEND_FAILED_500", 500, "메일 발송에 실패했습니다."),
    AUTH_CODE_GENERATION_FAILED_500("AUTH_CODE_GENERATION_FAILED_500", 500, "인증번호 생성에 실패했습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
