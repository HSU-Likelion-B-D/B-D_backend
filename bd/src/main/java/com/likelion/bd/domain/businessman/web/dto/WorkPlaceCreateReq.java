package com.likelion.bd.domain.businessman.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class WorkPlaceCreateReq {
    private Long userId; //user의 기본키 ID
    private String name; //사업장 이름
    private String address; //사업장 주소
    private String detailAddress; //사업장 상세주소
    private String openTime; //영업시작시간
    private String closeTime; //영업마감시간
    private Boolean isOnline; //온라인스토어 유무
    private String minBudget; //홍보 최저예산
    private String maxBudget; //홍보 최대예산
    private List<Long> categoryIds; //사업장 카테고리
    private List<Long> moodIds; //사업장 분위기
    private List<Long> promotionIds; //사업장 홍보방식

}
