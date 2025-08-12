package com.likelion.bd.domain.user.entity;

import com.likelion.bd.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "NICKNAME", nullable = false, unique = true)
    private String nickname;

    @Column(name = "PROFILE_IMAGE")
    private String profileImage;

    @Column(name = "INTRODUCTION")
    private String introduction;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private UserRoleType role;

    // -------------------------------------------------------------------------------------------------------

    // 회원 정보 수정을 위한 메서드
    public void updateProfile(String newNickname, String newImageUrl, String newIntroduction) {
        if (newNickname != null) {
            this.nickname = newNickname;
        }

        // 프로필 사진이랑 자기 소개글은 null 가능
        this.profileImage = newImageUrl;
        this.introduction = newIntroduction;
    }
}