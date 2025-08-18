package com.likelion.bd.domain.campaign.entity;

import com.likelion.bd.domain.user.entity.UserRoleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAMPAIGN_ID")
    private Long campaignId;

    @Column(name = "SENDER_ID", nullable = false)
    private Long senderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "SENDER_ROLE", nullable = false)
    private UserRoleType senderRole;

    @Column(name = "RECEIVER_ID", nullable = false)
    private Long receiverId;

    @Enumerated(EnumType.STRING)
    @Column(name = "RECEIVER_ROLE", nullable = false)
    private UserRoleType receiverRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATE")
    private CampaignStatus state;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proposalId")
    private Proposal proposal;
}
