package com.likelion.bd.domain.campaign.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private Long paymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "BUSINESSMAN_STATE", nullable = false)
    private PaymentStatus businessManState;

    @Enumerated(EnumType.STRING)
    @Column(name = "INFLUENCER_STATE", nullable = false)
    private PaymentStatus influencerState;

    @Column(name = "FEE", nullable = false)
    private int fee; // 수수료

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaignId", nullable = false)
    private Campaign campaign;

    public void updateBState(PaymentStatus paymentStatus) {
        this.businessManState = paymentStatus;
    }

    public void updateIState(PaymentStatus paymentStatus) {
        this.influencerState = paymentStatus;
    }
}
