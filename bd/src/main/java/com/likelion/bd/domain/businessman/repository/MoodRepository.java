package com.likelion.bd.domain.businessman.repository;

import com.likelion.bd.domain.businessman.entity.Mood;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoodRepository extends JpaRepository<Mood, Long> {
}
