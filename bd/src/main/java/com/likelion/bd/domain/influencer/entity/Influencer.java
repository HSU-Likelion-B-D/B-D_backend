package com.likelion.bd.domain.influencer.entity;

import com.likelion.bd.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Influencer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INFLUENCER_ID")
    private Long influencerId; //기본키

    @OneToOne
    @JoinColumn(name = "userId") //user 엔티티의 기본키 이름인 userId와 매핑한다.
    private User user;

    //https://github.com/HSU-Likelion-B-D/B-D_backend.git
}
