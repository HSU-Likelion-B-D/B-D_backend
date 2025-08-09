package com.likelion.bd.domain.influencer.entity;

import com.likelion.bd.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "activity")
public class Activity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACTIVITY_ID")
    private Long activityId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "INFLUENCER_ID", nullable = false)
    private Influencer influencer;

    @Column(name = "SNS_URL", nullable = false)
    private String snsUrl; // SNS 주소

    @Column(name = "FOLLOWER_COUNT", nullable = false)
    private Long followerCount; // 팔로워 수

    @Column(name = "ACTIVITY_NAME", nullable = false)
    private String activityName; // 활동명

    @Column(name = "ACCOUNT", nullable = false)
    private String account; // 계좌
}



