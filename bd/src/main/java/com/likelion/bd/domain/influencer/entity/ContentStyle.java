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
@Table(name = "contentStyle")
public class ContentStyle {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTENTSTYLE_ID")
    private Long contentStyleId;

    @Column(name = "NAME", nullable = false)
    private String name; // 컨텐츠 스타일 이름
}
