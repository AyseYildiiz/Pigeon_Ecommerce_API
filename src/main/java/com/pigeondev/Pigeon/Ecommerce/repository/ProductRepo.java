package com.pigeondev.Pigeon.Ecommerce.repository;

import com.pigeondev.Pigeon.Ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByNameContainingOrDescriptionContaining(String name, String description);
}
