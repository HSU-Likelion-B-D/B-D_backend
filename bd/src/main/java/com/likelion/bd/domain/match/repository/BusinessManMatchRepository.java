package com.likelion.bd.domain.match.repository;

import com.likelion.bd.domain.businessman.entity.BusinessMan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;


public interface BusinessManMatchRepository extends JpaRepository<BusinessMan, Long> {
    //지금 당장은 BusinessManRepository 사용해도 되지만 나중에 쿼리를 직접 작성하여 최적화할때는 여기 있는 레포지토리 사용해야함

    // 인플루언서가 가진 "콘텐츠 주제 이름" 세트로 자영업자 후보군 1차 필터
    @Query("""
        SELECT DISTINCT b
        FROM BusinessMan b
        JOIN b.workPlace w
        JOIN w.categoryList wpc
        JOIN wpc.category c
        WHERE c.name in :categoryNames
       """)
    List<BusinessMan> findBusinessManByCategoryNames(@Param("categoryNames") Set<String> categoryNames);
}
