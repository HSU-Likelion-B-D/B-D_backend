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
public class WorkPlaceMood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workplace_id", nullable = false)
    private WorkPlace workPlace;

    @ManyToOne
    @JoinColumn(name = "mood_id", nullable = false)
    private Mood mood;
}
