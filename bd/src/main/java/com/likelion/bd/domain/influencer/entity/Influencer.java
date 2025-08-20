package com.likelion.bd.domain.influencer.entity;

import com.likelion.bd.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "influencer")
public class Influencer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INFLUENCER_ID")
    private Long InfluencerId;

    /**
     * 인플루언서의 로그인 및 계정 관리를 위한 User 엔티티.
     * 모든 인플루언서는 반드시 하나의 User 계정을 가져야 함.
     * optional = false: 엔티티의 연관관계가 절대 null 이 될 수 없음을 의미하여 Hibernate 가 INNER JOIN 을 선택한다.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", unique = true, nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ACTIVITY_ID", unique = true, nullable = false)
    private Activity activity;

    @Column(name = "total_score")
    private Double totalScore;

    @Column(name = "review_count")
    private Long reviewCount;

    public void addReview(Double score) {
        this.totalScore += score;
        this.reviewCount++;
    }
}
