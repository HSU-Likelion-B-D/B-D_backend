package com.likelion.bd.domain.businessman.entity;

import com.likelion.bd.domain.user.entity.User;
import com.likelion.bd.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessMan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BUSINESSMAN_ID")
    private Long businessManId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId")
    private User user;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workplaceId")
    private WorkPlace workPlace;

    @Column(name = "total_score")
    private Double totalScore;

    @Column(name = "review_count")
    private Long reviewCount;

    public String formatAverageScore(Double totalScore, Long reviewCount) {
        //소수점 2자리까지 처리한다.
        BigDecimal avgScore = BigDecimal.ZERO;
        if (reviewCount > 0) {
            avgScore = BigDecimal.valueOf(totalScore)
                    .divide(BigDecimal.valueOf(reviewCount), 2, RoundingMode.HALF_UP);
        }

        return String.format("%.2f", avgScore);
    }

    public void addReview(Double score) {
        this.totalScore += score;
        this.reviewCount++;
    }
}
