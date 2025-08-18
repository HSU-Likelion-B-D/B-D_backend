package com.likelion.bd.global.exception;

import com.likelion.bd.global.response.code.BaseResponseCode;

public class CustomException extends BaseException {

    public CustomException(BaseResponseCode errorCode) {
        super(errorCode);
    }
}