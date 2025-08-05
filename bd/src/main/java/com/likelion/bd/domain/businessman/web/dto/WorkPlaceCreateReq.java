package com.likelion.bd.domain.businessman.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class WorkPlaceCreateReq {
    String name; //사업장 이름
    String address; //사업장 주소
    String detailAddress; //사업장 상세주소
    String openTime; //영업시작시간
    String closeTime; //영업마감시간
    Boolean isOnline; //온라인스토어 유무
    List<Long> categoryIds; //사업장 카테고리
    List<Long> moodIds; //사업장 분위기
    List<Long> promotionIds; //사업장 홍보방식

}
