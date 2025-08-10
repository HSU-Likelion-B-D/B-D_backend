package com.likelion.bd.global.response.code.Influencer;

import com.likelion.bd.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityErrorCode implements BaseResponseCode {

    PLATFORM_NOT_FOUND_404("PLATFORM_NOT_FOUND_404",404,"해당 플랫폼이 존재하지 않습니다."),
    CONTENTTOPIC_NOT_FOUND_404("CONTENTTOPIC_NOT_FOUND_404",404,"해당 콘텐츠 분야가 존재하지 않습니다."),
    CONTENTSTYLE_NOT_FOUND_404("CONTENTSTYLE_NOT_FOUND_404",404,"해당 콘텐스 스타일이 존재하지 않습니다."),
    PREPERTOPIC_NOT_FOUND_404("PREPERTOPIC_NOT_FOUND_404",404,"해당 선호 분야가 존재하지 않습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
