package com.likelion.bd.global.exception;

import com.likelion.bd.global.response.code.BaseResponseCode;
import com.likelion.bd.global.response.code.ErrorResponseCode;

public class CustomException extends RuntimeException{
    private final BaseResponseCode errorCode;

    public CustomException(BaseResponseCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BaseResponseCode getErrorCode() {
        return errorCode;
    }
}
