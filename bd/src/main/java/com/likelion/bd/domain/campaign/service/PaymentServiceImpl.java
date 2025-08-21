package com.likelion.bd.domain.campaign.service;

import com.likelion.bd.domain.campaign.entity.Campaign;
import com.likelion.bd.domain.campaign.entity.Payment;
import com.likelion.bd.domain.campaign.entity.PaymentStatus;
import com.likelion.bd.domain.campaign.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final int FEE = 10;

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void createPayment(Campaign campaign) {
        Payment payment = Payment.builder()
                .businessManState(PaymentStatus.WAITING)
                .influencerState(PaymentStatus.PAYMENT_PENDING)
                .fee(FEE)
                .campaign(campaign)
                .build();

        paymentRepository.save(payment);
    }
}
