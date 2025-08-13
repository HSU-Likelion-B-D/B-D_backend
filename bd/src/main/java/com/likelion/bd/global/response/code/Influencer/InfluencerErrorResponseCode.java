package com.likelion.bd.global.response.code.Influencer;

import com.likelion.bd.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InfluencerErrorResponseCode implements BaseResponseCode {

    INFLUENCER_NOT_FOUND_404("INFLUENCER_NOT_FOUND_404",404,"해당 인플루언서를 찾을 수 없습니다." ),
    INFLUENCER_DUPLICATE_409("INFLUENCER_DUPLICATE_409",409,"이미 인플루언서 정보가 존재합니다." );

    private final String code;
    private final int httpStatus;
    private final String message;
}
