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
@Table(name = "platform")
public class Platform {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLATFORM_ID")
    private Long platformId;

    @Column(name = "NAME", nullable = false)
    private String name; // 플랫폼 이름
}
