package com.likelion.bd.domain.influencer.repository;

import com.likelion.bd.domain.influencer.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {

}
