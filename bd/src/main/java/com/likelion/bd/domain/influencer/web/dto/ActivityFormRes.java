package com.likelion.bd.domain.influencer.web.dto;

import java.util.List;

public record ActivityFormRes(
        Long influencerId,
        String activityName,
        Long followerCount,
        int uploadFrequency,
        String bankName,
        String accountNumber,
        String minBudget,
        String maxBudget,
        List<String> platformIds,
        List<String> contentTopicIds,
        List<String> contentStyleIds,
        List<String> preferTopicIds
) {
}
