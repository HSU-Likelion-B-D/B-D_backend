package com.likelion.bd.global.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorResponseCode implements BaseResponseCode {

    AUTHENTICATION_FAILED_401("AUTHENTICATION_FAILED_401", 401, "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해 주세요."),
    INVALID_SIGNATURE_401("INVALID_SIGNATURE_401", 401, "유효하지 않은 JWT 서명입니다."),
    MALFORMED_TOKEN_401("MALFORMED_TOKEN_401", 401, "잘못된 형식의 JWT 토큰입니다."),
    EXPIRED_TOKEN_401("EXPIRED_TOKEN_401", 401, "만료된 JWT 토큰입니다."),
    UNSUPPORTED_TOKEN_401("UNSUPPORTED_TOKEN_401", 401, "지원하지 않는 JWT 토큰입니다."),
    ILLEGAL_ARGUMENT_401("ILLEGAL_ARGUMENT_401", 401, "JWT 클레임 문자열이 비어있습니다."),
    FORBIDDEN_403("FORBIDDEN_403", 403, "접근 권한이 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
