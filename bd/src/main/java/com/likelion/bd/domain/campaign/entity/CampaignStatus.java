package com.likelion.bd.domain.campaign.entity;

public enum CampaignStatus {
    WAITING("대기중"),
    PROPOSED("제안 받음"),
    CONFIRMED_IN_PROGRESS("확정 및 진행중"),
    COMPLETED("완료"),
    CANCELED("취소");

    //PAYMENT_PENDING("입금 대기");
    //ACCEPTED("수락"), REJECTED("거절")


    private final String koLabel;

    CampaignStatus(String koLabel) { this.koLabel = koLabel; }
    public String getKoLabel() { return koLabel; }
}
