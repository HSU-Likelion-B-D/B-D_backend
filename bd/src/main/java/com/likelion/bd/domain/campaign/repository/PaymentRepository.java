package com.likelion.bd.domain.campaign.repository;

import com.likelion.bd.domain.campaign.entity.CampaignStatus;
import com.likelion.bd.domain.campaign.entity.Payment;
import com.likelion.bd.domain.campaign.entity.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p " +
            "JOIN FETCH p.campaign c " +
            "WHERE (c.senderId = :userId OR c.receiverId = :userId)" +
            "AND (:all = true OR p.businessManState = :status)")
    Page<Payment> findPaymentsForBusiness(@Param("userId") Long userId,
                                          @Param("status") PaymentStatus status,
                                          @Param("all") boolean all,
                                          Pageable pageable);

    @Query("SELECT p FROM Payment p " +
            "JOIN FETCH p.campaign c " +
            "WHERE (c.senderId = :userId OR c.receiverId = :userId)" +
            "AND (:all = true OR p.influencerState = :status)")
    Page<Payment> findPaymentsForInfluencer(@Param("userId") Long userId,
                                            @Param("status") PaymentStatus status,
                                            @Param("all") boolean all,
                                            Pageable pageable);
}
