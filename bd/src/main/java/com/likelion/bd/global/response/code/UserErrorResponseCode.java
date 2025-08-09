package com.likelion.bd.global.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorResponseCode implements BaseResponseCode{
    USER_NOT_FOUND_404("USER_NOT_FOUND_404",404,"사용자 정보를 찾을 수 없습니다." ),
    NO_BUSINESS_PERMISSION_403("USER_403_1", 403, "사업장 등록 권한이 없습니다.");
    private final String code;
    private final int httpStatus;
    private final String message;
}
