package com.likelion.bd.domain.user.exception;

import com.likelion.bd.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseResponseCode {

    USER_INVALID_PASSWORD_401("USER_INVALID_PASSWORD_401", 401, "비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND_EMAIL_404("USER_NOT_FOUND_EMAIL_404", 404, "해당 이메일이 존재하지 않습니다."),
    USER_DUPLICATE_EMAIL_409("USER_DUPLICATE_EMAIL_409", 409, "이미 존재하는 이메일입니다."),
    USER_DUPLICATE_NICKNAME_409("USER_DUPLICATE_NICKNAME_409", 409, "이미 존재하는 닉네임입니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
