package com.likelion.bd.global.response.code.campaign;

import com.likelion.bd.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProposalErrorResponseCode implements BaseResponseCode {

    PROPOSAL_ALREADY_EXISTS_409("PROPOSAL_ALREADY_EXISTS_409", 409, "이미 제안서가 존재합니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
