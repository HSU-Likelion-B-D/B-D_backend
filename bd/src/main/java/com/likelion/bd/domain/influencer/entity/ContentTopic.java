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
@Table(name = "contentTopic")
public class ContentTopic {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTENTTOPIC_ID")
    private Long contentTopicId;

    @Column(name = "NAME", nullable = false)
    private String name; // 컨텐츠 분야 이름
}
