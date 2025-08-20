package com.likelion.bd.domain.businessman.repository;

import com.likelion.bd.domain.businessman.entity.BusinessMan;
import com.likelion.bd.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BusinessManRepository extends JpaRepository<BusinessMan, Long> {
    Optional<BusinessMan> findByUser(User user);
    Optional<BusinessMan> findByUserUserId(Long userId);

    @Query("SELECT b FROM BusinessMan b WHERE b.reviewCount > 0 ORDER BY (b.totalScore / b.reviewCount) DESC")
    List<BusinessMan> findTopBusinessManAverageScoreDesc(Pageable pageable);
}
