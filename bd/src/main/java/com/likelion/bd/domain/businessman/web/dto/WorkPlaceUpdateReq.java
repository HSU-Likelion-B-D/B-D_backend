package com.likelion.bd.domain.businessman.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class WorkPlaceUpdateReq {
    private Long userId; //수정 요청자
    private String name;
    private String address;
    private String detailAddress;
    private String openTime; // "HH:mm"
    private String closeTime;
    private Boolean isOnline;
    private List<Long> categoryIds;
    private List<Long> moodIds;
    private List<Long> promotionIds;
}
