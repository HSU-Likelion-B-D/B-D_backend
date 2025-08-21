package com.likelion.bd.domain.businessman.repository;

import com.likelion.bd.domain.businessman.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByNameIn(List<String> name);
}
