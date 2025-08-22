package com.likelion.bd.domain.campaign.service;

import com.likelion.bd.domain.campaign.entity.Campaign;
import com.likelion.bd.domain.campaign.entity.CampaignStatus;
import com.likelion.bd.domain.campaign.web.dto.PaymentListRes;
import com.likelion.bd.domain.campaign.web.dto.PaymentResponseReq;
import com.likelion.bd.global.jwt.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    void createPayment(Campaign campaign);

    Page<PaymentListRes> showPayment(
            UserPrincipal userPrincipal,
            CampaignStatus status,
            Boolean all,
            Pageable pageable);

    void updatePayment(UserPrincipal userPrincipal, PaymentResponseReq paymentResponseReq);
}
