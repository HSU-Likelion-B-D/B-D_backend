package com.likelion.bd.domain.match.repository;

import com.likelion.bd.domain.influencer.entity.Influencer;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface InfluencerMatchRepository extends JpaRepository<Influencer, Long> {
    //지금 당장은 InfluencerRepository 사용해도 되지만 나중에 쿼리를 직접 작성하여 최적화할때는 여기 있는 레포지토리 사용해야함

    @Query("SELECT DISTINCT i FROM Influencer i " +
    "LEFT JOIN i.activity a " +
    "LEFT JOIN  a.activityContentTopicList act " +
    "LEFT JOIN act.contentTopic ct " +
    "WHERE ct.name IN :categoryNames")
    List<Influencer> findInfluencerByCategoryNames(@Param("categoryNames") Set<String> categoryNames);
}
