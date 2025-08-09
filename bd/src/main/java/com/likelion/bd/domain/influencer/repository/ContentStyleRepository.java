package com.likelion.bd.domain.influencer.repository;

import com.likelion.bd.domain.influencer.entity.ContentStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentStyleRepository extends JpaRepository<ContentStyle, Long> {

}
