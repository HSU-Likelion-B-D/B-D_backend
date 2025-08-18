package com.likelion.bd.domain.influencer.repository;

import com.likelion.bd.domain.influencer.entity.PreferTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferTopicRepository extends JpaRepository<PreferTopic, Long> {
}
