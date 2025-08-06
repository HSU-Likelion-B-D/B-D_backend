package com.likelion.bd.domain.businessman.entity;

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
public class WorkPlacePromotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workplace_id", nullable = false)
    private WorkPlace workplace;

    @ManyToOne
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    public WorkPlacePromotion(WorkPlace workPlace, Promotion promotion) {
        this.workplace = workPlace;
        this.promotion = promotion;
    }
}
