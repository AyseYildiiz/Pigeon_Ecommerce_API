package com.pigeondev.Pigeon.Ecommerce.service.impl;

import com.pigeondev.Pigeon.Ecommerce.dto.CategoryDto;
import com.pigeondev.Pigeon.Ecommerce.dto.Response;
import com.pigeondev.Pigeon.Ecommerce.entity.Category;
import com.pigeondev.Pigeon.Ecommerce.exception.NotFoundException;
import com.pigeondev.Pigeon.Ecommerce.mapper.EntityDtoMapper;
import com.pigeondev.Pigeon.Ecommerce.repository.CategoryRepo;
import com.pigeondev.Pigeon.Ecommerce.service.interf.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Response createCategory(CategoryDto categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        categoryRepo.save(category);

        return Response.builder()
                .status(200)
                .message("Category created")
                .build();
    }

    @Override
    public Response updateCategory(Long categoryId, CategoryDto categoryRequest) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
        category.setName(categoryRequest.getName());
        categoryRepo.save(category);

        return Response.builder()
                .status(200)
                .message("Category updated")
                .build();
    }

    @Override
    public Response getAllCategories() {
        List<Category> categories = categoryRepo.findAll();
        List<CategoryDto> categoryDtoList = categories.stream()
                .map(entityDtoMapper::mapCategoryToDtoBasic)
                .toList();

        return Response.builder()
                .status(200)
                .categoryList(categoryDtoList)
                .build();
    }

    @Override
    public Response getCategoryById(Long categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
        CategoryDto categoryDto = entityDtoMapper.mapCategoryToDtoBasic(category);

        return Response.builder()
                .status(200)
                .category(categoryDto)
                .build();
    }

    @Override
    public Response deleteCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
        categoryRepo.delete(category);

        return Response.builder()
                .status(200)
                .message("Category was successfully deleted")
                .build();
    }
}
