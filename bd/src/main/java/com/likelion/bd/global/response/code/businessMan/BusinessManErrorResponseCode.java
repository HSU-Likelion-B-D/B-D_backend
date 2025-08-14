package com.likelion.bd.global.response.code.businessMan;

import com.likelion.bd.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessManErrorResponseCode implements BaseResponseCode {
    BUSINESSMAN_NOT_FOUND_404("BUSINESSMAN_NOT_FOUND_404",404,"자영업자 정보를 찾을 수 없습니다." ),
    BAD_REQUEST_400("BAD_REQUEST_400", 400, "유저 id가 비어 있습니다.");
    private final String code;
    private final int httpStatus;
    private final String message;
}
