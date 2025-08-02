package com.likelion.bd.domain.influencer.entity;

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
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACTIVITY_ID")
    private Long activityId; //Activity 엔티티 기본키

    @Column(name = "REGION", nullable = false)
    private String region; //인플루언서 활동 지역

    @Column(name = "DETAIL_ADDRESS", nullable = false)
    private String detailAddress; //인플루언서 활동 상세 주소

    @Column(name = "SNS_URL", nullable = false)
    private String snsUrl; //인플루언서 sns url 주소

    @Column(name = "FOLLOWER_COUNT", nullable = false)
    private Long followerCount; //인플루언서 sns 팔로워 수

    @Column(name = "ACTIVITY_NAME", nullable = false)
    private String activityName; //인플루언서 활동명

    @OneToOne
    @JoinColumn(name = "influencerId")
    private Influencer influencer; //인플루언서 id 외래키

}
