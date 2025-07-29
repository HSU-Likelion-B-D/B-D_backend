package com.likelion.bd.global.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.likelion.bd.global.response.code.BaseResponseCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonPropertyOrder({"isSuccess", "timestamp", "code", "httpStatus", "message", "data"})
public class ErrorResponse<T> extends BaseResponse {

    private final int httpStatus;
    private final T data;

    @Builder
    public ErrorResponse(T data, BaseResponseCode baseResponseCode) {
        super(false, baseResponseCode.getCode(), baseResponseCode.getMessage());
        this.httpStatus = baseResponseCode.getHttpStatus();
        this.data = data;
    }

    @Builder
    public ErrorResponse(T data, BaseResponseCode baseResponseCode, String message) { // 메세지 커스텀 가능
        super(false, baseResponseCode.getCode(), message);
        this.httpStatus = baseResponseCode.getHttpStatus();
        this.data = data;
    }

    public static ErrorResponse<?> from(BaseResponseCode baseResponseCode) { // data X, message 기본
        return new ErrorResponse<>(null, baseResponseCode);
    }

    public static ErrorResponse<?> of(BaseResponseCode baseResponseCode, String message) { // data X, message 커스텀
        return new ErrorResponse<>(null, baseResponseCode, message);
    }

    public static <T> ErrorResponse<T> of(BaseResponseCode baseResponseCode, T data) { // data O, baseResponseCode 기본
        return new ErrorResponse<>(data, baseResponseCode);
    }

    public static <T> ErrorResponse<T> of(BaseResponseCode baseResponseCode, T data, String message) { // data O, message 커스텀
        return  new ErrorResponse<>(data, baseResponseCode, message);
    }
}
