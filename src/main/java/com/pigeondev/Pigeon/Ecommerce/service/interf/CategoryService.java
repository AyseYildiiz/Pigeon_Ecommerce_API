package com.pigeondev.Pigeon.Ecommerce.service.interf;


import com.pigeondev.Pigeon.Ecommerce.dto.CategoryDto;
import com.pigeondev.Pigeon.Ecommerce.dto.Response;

public interface CategoryService {
    Response createCategory(CategoryDto categoryRequest);

    Response updateCategory(Long categoryId, CategoryDto categoryRequest);

    Response getAllCategories();

    Response getCategoryById(Long categoryId);

    Response deleteCategory(Long categoryId);
}
