package com.likelion.bd.domain.influencer.web.dto;

import java.util.List;

public record InfluencerHomeRes(
        String imgUrl,
        String nickName,
        String activityName,
        String avgScore,
        Long reviewCount,
        List<BusinessManSummaryRes> businessMans
) {
    public record BusinessManSummaryRes(
            Long businessManId,
            String nickName,
            String imgUrl,
            String avgScore,
            Long reviewCount
    ) {

    }
}

/*
프로필 사진
닉네임
활동명
평점
리뷰수
 */