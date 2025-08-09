package com.likelion.bd.domain.influencer.repository;

import com.likelion.bd.domain.influencer.entity.ContentTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentTopicRepository extends JpaRepository<ContentTopic, Long> {

}
