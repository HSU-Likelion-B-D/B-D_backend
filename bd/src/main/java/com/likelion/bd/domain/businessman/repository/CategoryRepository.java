package com.likelion.bd.domain.businessman.repository;

import com.likelion.bd.domain.businessman.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
