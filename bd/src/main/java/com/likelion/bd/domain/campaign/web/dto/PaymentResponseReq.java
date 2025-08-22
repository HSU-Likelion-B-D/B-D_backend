package com.likelion.bd.domain.campaign.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentResponseReq {

    @NotNull(message = "Payment ID는 필수 입력 값 입니다.")
    private Long paymentId;
}
