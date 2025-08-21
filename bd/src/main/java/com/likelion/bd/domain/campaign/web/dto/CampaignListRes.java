package com.likelion.bd.domain.campaign.web.dto;

import java.time.LocalDate;

public record CampaignListRes(
        Long campaignId,
        String imgUrl,
        String title,
        String offerBudget,
        LocalDate startDate,
        LocalDate endDate,
        String status
) {

}
