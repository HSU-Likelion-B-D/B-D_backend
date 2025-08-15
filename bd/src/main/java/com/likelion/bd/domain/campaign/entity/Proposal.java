package com.likelion.bd.domain.campaign.entity;

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

    @Column(name = "WRITER_ID", nullable = false, unique = true)
    private Long writerId; // 작성자 ID

    @Column(name = "TITLE")
    private String title; // 제안서 제목

    @Column(name = "MIN_AMOUNT")
    private Long minAmount; // 최소 금액

    @Column(name = "MAX_AMOUNT")
    private Long maxAmount; // 최대 금액

    @Column(name = "START_DATE", nullable = false)
    private LocalDate startDate; // 시작 날짜

    @Column(name = "END_DATE", nullable = false)
    private LocalDate endDate; // 종료 날짜

    @Column(name = "OVER_VIEW", nullable = false)
    private String overView; // 개요

    @Column(name = "REQUEST")
    private String request; // 요청사항
}

/*
제안서 제목
최소 금액
최대 금액
시작 날짜, str
종료 날짜, str
제안서 개요
요청사항
-------------------------------
제안자 명 -> 안 넣음
(자영업자)가게 위치 -> 안 넣음
(인플루언서)활동 플랫폼 -> 안 넣음
(인플루언서)콘텐츠 분야 -> 안 넣음
 */