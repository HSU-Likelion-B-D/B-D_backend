package com.likelion.bd.domain.influencer.entity;

import com.likelion.bd.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "ACTIVITY_NAME", nullable = false)
    private String activityName; // 활동명

    @Column(name = "SNS_URL", nullable = false)
    private String snsUrl; // SNS 주소

    @Enumerated(EnumType.STRING)
    @Column(name = "FOLLOWER_COUNT_RANGE", nullable = false)
    private FollowerCountRange followerCountRange; // 팔로워 수 범위

    @Enumerated(EnumType.STRING)
    @Column(name = "UPLOAD_FREQUENCY", nullable = false)
    private UploadFrequency uploadFrequency; // 주 업로드 횟수

    @Column(name = "BANKNAME", nullable = false)
    private String bankName; // 은행명

    @Column(name = "ACCOUNT_NUMBER", nullable = false)
    private String accountNumber; // 계좌번호

    @Column(name = "MIN_AMOUNT", nullable = false)
    private Long minAmount; // 최소 희망 금액

    @Column(name = "MAX_AMOUNT", nullable = false)
    private Long maxAmount; // 최대 희망 금액

    // -------------------------------------------------------------------------------------

    @Builder.Default
    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityPlatform> activityPlatformList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityContentTopic> activityContentTopicList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityContentStyle> activityContentStyleList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityPreferTopic> activityPreferTopicList = new ArrayList<>();

    // -------------------------------------------------------------------------------------

    public void addActivityPlatform(ActivityPlatform activityPlatform) {
        this.activityPlatformList.add(activityPlatform);
        activityPlatform.setActivity(this);
    }

    public void addActivityContentTopic(ActivityContentTopic activityContentTopic) {
        this.activityContentTopicList.add(activityContentTopic);
        activityContentTopic.setActivity(this);
    }

    public void addActivityContentStyle(ActivityContentStyle activityContentStyle) {
        this.activityContentStyleList.add(activityContentStyle);
        activityContentStyle.setActivity(this);
    }

    public void addActivityPreferTopic(ActivityPreferTopic activityPreferTopic) {
        this.activityPreferTopicList.add(activityPreferTopic);
        activityPreferTopic.setActivity(this);
    }
}



