package com.likelion.bd.domain.user.repository;

import com.likelion.bd.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    // User 라는 객체가 존재하면 값을 담고, 존재하지 않으면 비어있는 상태로 반환
    Optional<User> findByEmail(String email);
    Optional<User> findByUserId(Long userId);

    User getUserByUserId(Long userId);
}
