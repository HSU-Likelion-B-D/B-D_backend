package com.likelion.bd.domain.campaign.repository;

import com.likelion.bd.domain.campaign.entity.Campaign;
import com.likelion.bd.domain.campaign.entity.CampaignStatus;
import com.likelion.bd.domain.user.entity.UserRoleType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    /*
     * 모두보기(all=true): 상태 무시.
     * ((senderId = :userId AND senderRole = :role) OR (receiverId = :userId AND receiverRole = :role))
     * N+1 방지를 위해 proposal fetch.
     */
    @EntityGraph(attributePaths = "proposal")
    @Query(value = """
        SELECT DISTINCT c
        FROM Campaign c
        WHERE ( (c.senderId   = :userId AND c.senderRole   = :role)
             OR (c.receiverId = :userId AND c.receiverRole = :role) )
        """,
            countQuery = """
        SELECT COUNT(DISTINCT c.campaignId)
        FROM Campaign c
        WHERE ( (c.senderId   = :userId AND c.senderRole   = :role)
             OR (c.receiverId = :userId AND c.receiverRole = :role) )
        """
    )
    Page<Campaign> findMyCampaignsAll(@Param("userId") Long userId,
                                      @Param("role") UserRoleType role,
                                      Pageable pageable);

    /*
     * 상태 지정(모두보기 아님): state까지 필터.
     * ((senderId = :userId AND senderRole = :role) OR (receiverId = :userId AND receiverRole = :role))
     * AND c.state = :state
     */
    @EntityGraph(attributePaths = "proposal")
    @Query(value = """
        SELECT DISTINCT c
        FROM Campaign c
        WHERE ( (c.senderId   = :userId AND c.senderRole   = :role)
             OR (c.receiverId = :userId AND c.receiverRole = :role) )
          AND c.state = :state
        """,
            countQuery = """
        SELECT COUNT(DISTINCT c.campaignId)
        FROM Campaign c
        WHERE ( (c.senderId   = :userId AND c.senderRole   = :role)
             OR (c.receiverId = :userId AND c.receiverRole = :role) )
          AND c.state = :state
        """
    )
    Page<Campaign> findMyCampaignsByState(@Param("userId") Long userId,
                                          @Param("role") UserRoleType role,
                                          @Param("state") CampaignStatus state,
                                          Pageable pageable);


    // 발신자, 수신자, 제안서 ID가 모두 일치하는 캠페인이 존재하는지 확인
    boolean existsBySenderIdAndReceiverIdAndProposalProposalId(Long senderId, Long receiverId, Long proposalId);
}
