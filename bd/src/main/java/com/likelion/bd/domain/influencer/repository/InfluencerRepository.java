package com.likelion.bd.domain.influencer.repository;

import com.likelion.bd.domain.influencer.entity.Influencer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfluencerRepository extends JpaRepository<Influencer, Long> {
}
