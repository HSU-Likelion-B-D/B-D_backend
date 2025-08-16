package com.likelion.bd.domain.campaign.repository;

import com.likelion.bd.domain.campaign.entity.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProposalRepository  extends JpaRepository<Proposal, Long> {

    Optional<Proposal> findByWriterId(Long userId);
}
