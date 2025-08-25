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
    @Column(name = "sender_State")
    private CampaignStatus senderState;

    @Enumerated(EnumType.STRING)
    @Column(name = "receiver_State")
    private CampaignStatus receiverState;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proposalId", nullable = false)
    private Proposal proposal;

    public void updateState(String state) {
        if (state.equals("yes")) {
            this.senderState = CampaignStatus.CONFIRMED_IN_PROGRESS;
            this.receiverState = CampaignStatus.CONFIRMED_IN_PROGRESS;
        } else if (state.equals("no")) {
            this.senderState = CampaignStatus.CANCELED;
            this.receiverState = CampaignStatus.CANCELED;
        }
    }
}
