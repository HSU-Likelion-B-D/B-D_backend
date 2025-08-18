package com.likelion.bd.domain.campaign.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CampaignCreateReq {

    @NotNull(message = "제안서 ID는 필수 입력 값 입니다.")
    private Long proposalId;

    @NotNull(message = "받는 사람 ID는 필수 입력 값 입니다.")
    private Long recipientId;
}
