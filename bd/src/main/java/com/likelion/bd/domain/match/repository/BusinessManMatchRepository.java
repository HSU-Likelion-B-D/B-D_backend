package com.likelion.bd.domain.match.repository;

import com.likelion.bd.domain.businessman.entity.BusinessMan;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BusinessManMatchRepository extends JpaRepository<BusinessMan, Long> {
    //지금 당장은 BusinessManRepository 사용해도 되지만 나중에 쿼리를 직접 작성하여 최적화할때는 여기 있는 레포지토리 사용해야함
}
