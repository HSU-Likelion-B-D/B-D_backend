package com.likelion.bd.domain.campaign.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CampaignResponseReq {

    @NotNull(message = "캠페인 ID는 필수 입력 값 입니다.")
    private Long campaignId;

    @NotBlank(message = "역할은 필수 입력 값입니다.")
    @Pattern(regexp = "^(yes|no)$", message = "역할은 'yes' 또는 'no' 중 하나여야 합니다.")
    private String response;
}
