package com.likelion.bd.domain.businessman.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WORKPLACE_ID")
    private Long workplaceId;

    @Column(name = "NAME")
    private String name; //사업장 이름

    @Column(name = "ADDRESS")
    private String address; //사업장 주소

    @Column(name = "DETAIL_ADDRESS")
    private String detailAddress; //사업장 상세주소

    @Column(name = "OPEN_TIME")
    private LocalTime openTime; //사업장 오픈시간

    @Column(name = "CLOSE_TIME")
    private LocalTime closeTime; //사업장 마감시간

    @Column(name = "ONLINE_STORE")
    private Boolean onlineStore; //사업장 온라인스토어 유무

    @OneToOne
    @JoinColumn(name = "businessmanId")
    private BusinessMan businessman; //자영업자 외래키
}
