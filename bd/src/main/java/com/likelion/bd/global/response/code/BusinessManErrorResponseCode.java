package com.likelion.bd.global.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessManErrorResponseCode implements BaseResponseCode {
    BUSINESSMAN_NOT_FOUND_404("BUSINESSMAN_NOT_FOUND_404",404,"자영업자 정보를 찾을 수 없습니다." );
    private final String code;
    private final int httpStatus;
    private final String message;
}
