package com.likelion.bd.domain.businessman.repository;

import com.likelion.bd.domain.businessman.entity.BusinessMan;
import com.likelion.bd.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessManRepository extends JpaRepository<BusinessMan, Long> {
    Optional<BusinessMan> findByUser(User user);
}
