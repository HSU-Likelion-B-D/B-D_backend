package com.likelion.bd.domain.influencer.web.dto;

import java.util.List;

public record InfluencerMyPageRes(
        String imgUrl,                  // 사진
        String activityName,            // 활동명
        String introduce,               //자기소개
        String nickName,                // 닉네임
        String follower,                // 팔로워 수
        String avgScore,                // 평점
        Long reviewCount,               // 평가 받은 횟수
        String snsUrl,                  // SNS 링크
        Long minAmount,                 // 희망하는 최소 금액
        List<String> platforms,        // 플랫폼 목록
        List<String> contentTopics,    // 컨텐츠 분야 목록
        List<String> contentStyles,    // 컨텐츠 스타일 목록
        List<String> preferTopics      // 선호 분야 목록
) {

}