package com.likelion.bd.domain.match.repository;

import com.likelion.bd.domain.businessman.entity.BusinessMan;
import com.likelion.bd.domain.businessman.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BusinessManMatchRepository extends JpaRepository<BusinessMan, Long> {
    //지금 당장은 BusinessManRepository 사용해도 되지만 나중에 쿼리를 직접 작성하여 최적화할때는 여기 있는 레포지토리 사용해야함

    @Query("select DISTINCT b FROM BusinessMan b " +
            "LEFT JOIN FETCH b.workPlace w " +
            "LEFT JOIN FETCH w.categoryList wpc " +
            "LEFT JOIN FETCH wpc.category c " +
            "LEFT JOIN FETCH w.moodList wpm " +
            "LEFT JOIN FETCH wpm.mood m" +
            "LEFT JOIN FETCH w.promotionList wpp " +
            "LEFT JOIN FETCH wpp.promotion p " +
            "WHERE c IN :categories")
    List<BusinessMan> findBusinessManInCategoriesWithDetails(@Param("categories") List<Category> categories);
}
