package com.likelion.bd.domain.influencer.repository;

import com.likelion.bd.domain.influencer.entity.Influencer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InfluencerRepository extends JpaRepository<Influencer, Long> {

    Optional<Influencer> findByUserUserId(Long userId);
}
