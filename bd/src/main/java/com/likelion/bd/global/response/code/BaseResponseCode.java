package com.likelion.bd.global.response.code;

public interface BaseResponseCode {
    String getCode();
    int getHttpStatus();
    String getMessage();
}
