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
@Table(name = "adIndustry")
public class AdIndustry {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADINDUSTRY_ID")
    private Long adIndustryId;

    @Column(name = "NAME", nullable = false)
    private String name; // 광고 분야 이름
}
