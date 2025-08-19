package com.likelion.bd.domain.influencer.web.dto;

public record InfluencerHomeRes(
        String imgUrl,
        String nickName,
        String activityName,
        String avgScore,
        Long reviewCount
) {

}

/*
프로필 사진
닉네임
활동명
평점
리뷰수
 */