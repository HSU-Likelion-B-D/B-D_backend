package com.likelion.bd.domain.campaign.repository;

import com.likelion.bd.domain.campaign.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
