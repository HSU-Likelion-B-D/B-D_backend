package com.likelion.bd.global.response.code.campaign;

import com.likelion.bd.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CampaignErrorResponseCode implements BaseResponseCode {

    CAMPAIGN_NOT_FOUND_404("CAMPAIGN_NOT_FOUND_404", 404, "해당 캠페인을 찾을 수 없습니다."),
    CAMPAIGN_ALREADY_EXISTS_409("CAMPAIGN_ALREADY_EXISTS_409", 409, "이미 캠페인이 존재합니다."),
    SAME_ROLE_PROPOSAL_409("SAME_ROLE_PROPOSAL_409", 409, "동일 역할군에게는 제안서를 보낼 수 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
