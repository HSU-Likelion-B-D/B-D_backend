package com.likelion.bd.global.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.likelion.bd.global.response.code.BaseResponseCode;
import com.likelion.bd.global.response.code.SuccessResponseCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonPropertyOrder({"isSuccess", "timestamp", "code", "httpStatus", "message", "data"})
public class SuccessResponse<T>  extends BaseResponse {

    private final int httpStatus;
    private final T data;

    @Builder
    public SuccessResponse(T data, BaseResponseCode baseResponseCode) {
        super(true, baseResponseCode.getCode(), baseResponseCode.getMessage());
        this.httpStatus = baseResponseCode.getHttpStatus();
        this.data = data;
    }

    @Builder
    public SuccessResponse(T data, BaseResponseCode baseResponseCode, String message) { // 메세지 커스텀 가능
        super(true, baseResponseCode.getCode(), message);
        this.httpStatus = baseResponseCode.getHttpStatus();
        this.data = data;
    }

    public static SuccessResponse<?> empty() { // data X, message 기본
        return new SuccessResponse<>(null, SuccessResponseCode.SUCCESS_OK);
    }

    public static SuccessResponse<?> emptyCustom (String message) { // data X, message 커스텀
        return new SuccessResponse<>(null, SuccessResponseCode.SUCCESS_OK, message);
    }

    public static <T> SuccessResponse<T> ok(T data) { // data 0, message 기본
        return new SuccessResponse<>(data, SuccessResponseCode.SUCCESS_OK);
    }

    public static <T> SuccessResponse<T> okCustom(T data, String message) { // data 0, message 커스텀
        return new SuccessResponse<>(data, SuccessResponseCode.SUCCESS_OK, message);
    }

    // ---------------------------------------------------------------------------------------------------------

    public static SuccessResponse<?> from(BaseResponseCode baseResponseCode) {
        return new SuccessResponse<>(null, baseResponseCode);
    }

    public static SuccessResponse<?> of(BaseResponseCode baseResponseCode, String message) {
        return new SuccessResponse<>(null, baseResponseCode, message);
    }

    public static <T> SuccessResponse<T> of(T data, BaseResponseCode baseResponseCode) {
        return new SuccessResponse<>(data, baseResponseCode);
    }

    public static <T> SuccessResponse<T> of(T data, BaseResponseCode baseResponseCode, String message) {
        return new SuccessResponse<>(data, baseResponseCode, message);
    }
}
