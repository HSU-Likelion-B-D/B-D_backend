package com.likelion.bd.domain.businessman.entity;

import com.likelion.bd.domain.user.entity.User;
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
public class BusinessMan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BUSINESSMAN_ID")
    private Long businessManId;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;
}
