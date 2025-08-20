package com.likelion.bd.domain.campaign.web.dto;

import java.time.LocalDate;

public record CampaignListRes(
        Long campaignId,
        String imgUrl,
        String title,
        Long offerBudget,
        LocalDate startDate,
        LocalDate endDate,
        String status
) {

}
