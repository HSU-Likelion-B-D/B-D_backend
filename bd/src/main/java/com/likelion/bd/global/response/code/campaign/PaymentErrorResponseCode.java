package com.likelion.bd.global.response.code.campaign;

import com.likelion.bd.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentErrorResponseCode implements BaseResponseCode {

    PAYMENT_NOT_FOUND_404("PAYMENT_NOT_FOUND_404", 404, "해당 결제를 찾을 수 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
