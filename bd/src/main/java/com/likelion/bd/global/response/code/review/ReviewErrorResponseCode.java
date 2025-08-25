package com.likelion.bd.global.response.code.review;

import com.likelion.bd.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReviewErrorResponseCode implements BaseResponseCode {

    REVIEW_NOT_FOUND_404("REVIEW_NOT_FOUND_404", 404, "작성된 리뷰가 존재하지 않습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
