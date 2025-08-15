package com.likelion.bd.domain.campaign.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAMPAIGN_ID")
    private Long campaignId;

    @Column(name = "SENDER_ID", nullable = false)
    private Long senderId;

    @Column(name = "SENDER_ROLE", nullable = false)
    private String senderRole;

    @Column(name = "RECEIVER_ID", nullable = false)
    private Long receiverId;

    @Column(name = "RECEIVER_ROLE", nullable = false)
    private String receiverRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATE")
    private CampaignStatus state;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proposalId")
    private Proposal proposal;


}
