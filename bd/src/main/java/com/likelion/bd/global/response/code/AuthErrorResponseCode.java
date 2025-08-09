package com.likelion.bd.global.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.likelion.bd.global.constant.StaticValue.UNAUTHORIZED;

@Getter
@AllArgsConstructor
public enum AuthErrorResponseCode implements BaseResponseCode {

    AUTHENTICATION_FAILED("AUTHENTICATION_FAILED", UNAUTHORIZED, "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해 주세요."),
    INVALID_SIGNATURE("INVALID_SIGNATURE", 401, "유효하지 않은 JWT 서명입니다."),
    MALFORMED_TOKEN("MALFORMED_TOKEN", 401, "잘못된 형식의 JWT 토큰입니다."),
    EXPIRED_TOKEN("EXPIRED_TOKEN", 401, "만료된 JWT 토큰입니다."),
    UNSUPPORTED_TOKEN("UNSUPPORTED_TOKEN", 401, "지원하지 않는 JWT 토큰입니다."),
    ILLEGAL_ARGUMENT("ILLEGAL_ARGUMENT", 401, "JWT 클레임 문자열이 비어있습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
