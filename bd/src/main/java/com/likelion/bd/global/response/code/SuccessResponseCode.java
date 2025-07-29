package com.likelion.bd.global.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.likelion.bd.global.constant.StaticValue.*;

@Getter
@AllArgsConstructor
public enum SuccessResponseCode implements BaseResponseCode {

    SUCCESS_OK("SUCCESS_OK", OK, "호출에 성공하셨습니다."),
    SUCCESS_CREATED("SUCCESS_CREATED", CREATED, "생성에 성공하였습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;

}
