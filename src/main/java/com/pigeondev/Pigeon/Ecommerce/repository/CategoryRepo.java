package com.pigeondev.Pigeon.Ecommerce.repository;

import com.pigeondev.Pigeon.Ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {
}
