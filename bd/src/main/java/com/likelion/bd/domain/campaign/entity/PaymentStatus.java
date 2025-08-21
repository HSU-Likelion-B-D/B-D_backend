package com.likelion.bd.domain.campaign.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {

    PAYMENT_PENDING("결제하기"), // 자영업자 : 돈 지급해야함,
    PAYMENT_COMPLETED("결제 완료"), // 자영업자 : 돈 지급 완료
    WAITING("대기중"), // 인플루언서 : 자영업자가 돈을 지불하지 "않은" 상태
    PAYMENT_DUE("결제 받기"), // 인플루언서 : 자영업자가 돈을 지불"한" 상태
    SETTLEMENT_COMPLETED("정산 완료"); // 모두 : 돈을 주고, 받고 완료된 상태

    private final String description;
}
