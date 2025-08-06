package com.likelion.bd.global.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkPlaceErrorReponseCode implements BaseResponseCode{
    INVALID_TIME_FORMAT_400("INVALID_TIME_FORMAT_400",400,"시간 형식이 맞지 않습니다. ex) HH:MM"),
    CATEGORY_NOT_FOUND_404("CATEGORY_NOT_FOUND_404",404,"해당 카테고리가 존재하지 않습니다."),
    MOOD_NOT_FOUND_404("MOOD_NOT_FOUND_404", 404, "해당 분위기가 존재하지 않습니다."),
    PROMOTION_NOT_FOUND_404("PROMOTION_NOT_FOUND_404",404, "해당 홍보방식이 존재하지 않습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
