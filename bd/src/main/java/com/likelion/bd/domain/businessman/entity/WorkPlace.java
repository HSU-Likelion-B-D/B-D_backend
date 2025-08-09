package com.likelion.bd.domain.businessman.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "NAME", nullable = false)
    private String name; //사업장 이름

    @Column(name = "ADDRESS", nullable = false)
    private String address; //사업장 주소

    @Column(name = "DETAIL_ADDRESS")
    private String detailAddress; //사업장 상세주소

    @Column(name = "OPEN_TIME", nullable = false)
    private LocalTime openTime; //사업장 오픈시간

    @Column(name = "CLOSE_TIME", nullable = false)
    private LocalTime closeTime; //사업장 마감시간

    @Column(name = "ONLINE_STORE", nullable = false)
    private Boolean isOnline; //사업장 온라인스토어 유무

    @OneToOne
    @JoinColumn(name = "businessmanId")
    private BusinessMan businessman; //자영업자 외래키

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "businessman_id", nullable = false)
//    private BusinessMan businessman;

    @Builder.Default
    @OneToMany(mappedBy = "workPlace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkPlaceCategory> categoryList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "workPlace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkPlaceMood> moodList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "workPlace", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<WorkPlacePromotion> promotionList = new ArrayList<>();

    public void updateBasicInfo(String name, String address, String detailAddress,
                                LocalTime openTime, LocalTime closeTime, Boolean isOnline){
        this.name = name;
        this.address = address;
        this.detailAddress = detailAddress;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.isOnline = isOnline;
    }
}
