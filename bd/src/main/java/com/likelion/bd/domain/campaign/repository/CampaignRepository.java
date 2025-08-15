package com.likelion.bd.domain.campaign.repository;

import com.likelion.bd.domain.campaign.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
}
