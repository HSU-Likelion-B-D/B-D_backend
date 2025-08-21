package com.likelion.bd.domain.campaign.entity;

import com.likelion.bd.domain.campaign.web.dto.ProposalWriteReq;
import com.likelion.bd.domain.user.entity.UserRoleType;
import com.likelion.bd.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "proposal") // 제안서
public class Proposal extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROPOSAL_ID")
    private Long proposalId;

    @Column(name = "WRITER_ID", nullable = false)
    private Long writerId; // 작성자 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "WRITER_ROLE")
    private UserRoleType writeRole; // 작성자 역할

    @Column(name = "TITLE")
    private String title; // 제안서 제목

    @Column(name = "OFFER_BUDGET")
    private String offerBudget; // 제시 금액

    @Column(name = "START_DATE", nullable = false)
    private LocalDate startDate; // 시작 날짜

    @Column(name = "END_DATE", nullable = false)
    private LocalDate endDate; // 종료 날짜

    @Column(name = "OVER_VIEW", nullable = false)
    private String overView; // 개요

    @Column(name = "REQUEST")
    private String request; // 요청사항

    @Column(name = "CONTENT_TOPIC")
    private String contentTopic; // 인플루언서만 사용!

    // -------------------------------------------------------------------------------------

    // 제안서 수정 메소드
    public void updateProposal(ProposalWriteReq req, String role) {
        this.title = req.getTitle();
        this.offerBudget = req.getOfferBudget();
        this.startDate = LocalDate.parse(req.getStartDate());
        this.endDate = LocalDate.parse(req.getEndDate());
        this.overView = req.getOverView();
        this.request = req.getRequest();

        if (UserRoleType.valueOf(role) == UserRoleType.INFLUENCER) {
            if (req.getContentTopic() != null && !req.getContentTopic().isEmpty()) {
                this.contentTopic = req.getContentTopic();
            } else this.contentTopic = null;
        } else this.contentTopic = null; // 자영업자면 그냥 null
    }
}

/*
제안서 제목
제시 금액
시작 날짜, str
종료 날짜, str
제안서 개요
요청사항
콘텐츠 분야(인플루언서)
-------------------------------
제안자 명 -> 안 넣음
(자영업자)가게 위치 -> 안 넣음
(인플루언서)활동 플랫폼 -> 안 넣음
 */