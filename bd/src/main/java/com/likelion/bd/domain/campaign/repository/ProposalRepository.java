package com.likelion.bd.domain.campaign.repository;

import com.likelion.bd.domain.campaign.entity.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalRepository  extends JpaRepository<Proposal, Long> {
}
