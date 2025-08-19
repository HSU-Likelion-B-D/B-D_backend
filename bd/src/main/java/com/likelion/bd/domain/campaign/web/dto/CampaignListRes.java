package com.likelion.bd.domain.campaign.web.dto;

import java.time.LocalDate;

public record CampaignListRes(
        String imgUrl,
        String title,
        Long offerAmount,
        LocalDate startDate,
        LocalDate endDate,
        String status
) {

}
