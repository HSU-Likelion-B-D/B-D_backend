package com.likelion.bd.domain.review.entity;

import com.likelion.bd.domain.user.entity.UserRoleType;
import com.likelion.bd.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
public class Review extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_ID")
    private Long reviewId;

    @Column(name = "WRITER_ID", nullable = false)
    private Long writerId; // 작성자 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "WRITER_ROLE")
    private UserRoleType writeRole; // 작성자 역할

    @Column(name = "REVIEWED_ID", nullable = false)
    private Long reviewedId; // 리뷰 대상 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "REVIEWED_ROLE")
    private UserRoleType reviewedRole; // 리뷰 대상 역할

    @Column(name = "SCORE", nullable = false)
    private Double score; // 리뷰 점수

    @Column(name = "CONTENT", nullable = false, columnDefinition = "TEXT")
    private String content; // 리뷰 내용
}
