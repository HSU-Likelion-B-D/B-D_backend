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
@Table(name = "preferTopic")
public class PreferTopic {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PREFERTOPIC_ID")
    private Long preferTopicId;

    @Column(name = "NAME", nullable = false)
    private String name; // 선호 분야 이름
}
