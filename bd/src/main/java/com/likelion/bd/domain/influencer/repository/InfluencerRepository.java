package com.likelion.bd.domain.influencer.repository;

import com.likelion.bd.domain.influencer.entity.Influencer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InfluencerRepository extends JpaRepository<Influencer, Long> {

    Optional<Influencer> findByUserUserId(Long userId);

    @Query("SELECT i FROM Influencer i ORDER BY i.activity.followerCount DESC")
    List<Influencer> findTopInfluencerFollowerCountDesc(Pageable pageable);
}
