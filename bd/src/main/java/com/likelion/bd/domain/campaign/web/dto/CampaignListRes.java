package com.likelion.bd.domain.campaign.web.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record CampaignListRes(
        String imgUrl,
        String title,
        Long minBudget,
        Long maxBudget,
        LocalDate startDate,
        LocalDate endDate,
        String status
) {

}
