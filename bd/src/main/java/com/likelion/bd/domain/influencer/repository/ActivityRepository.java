package com.likelion.bd.domain.influencer.repository;

import com.likelion.bd.domain.influencer.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
