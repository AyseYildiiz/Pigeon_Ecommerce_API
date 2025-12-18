package com.pigeondev.Pigeon.Ecommerce.service.impl;

import com.pigeondev.Pigeon.Ecommerce.dto.ProductDto;
import com.pigeondev.Pigeon.Ecommerce.dto.Response;
import com.pigeondev.Pigeon.Ecommerce.entity.Category;
import com.pigeondev.Pigeon.Ecommerce.entity.Product;
import com.pigeondev.Pigeon.Ecommerce.exception.NotFoundException;
import com.pigeondev.Pigeon.Ecommerce.mapper.EntityDtoMapper;
import com.pigeondev.Pigeon.Ecommerce.repository.CategoryRepo;
import com.pigeondev.Pigeon.Ecommerce.repository.ProductRepo;
import com.pigeondev.Pigeon.Ecommerce.service.AwsS3Service;
import com.pigeondev.Pigeon.Ecommerce.service.interf.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final EntityDtoMapper entityDtoMapper;
    private final AwsS3Service awsS3Service;

    @Override
    public Response createProduct(Long categoryId, MultipartFile image, String name, String description, BigDecimal price) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
        String productImageUrl = awsS3Service.saveImageToS3(image);

        Product product = new Product();
        product.setCategory(category);
        product.setPrice(price);
        product.setName(name);
        product.setDescription(description);
        product.setImageURL(productImageUrl);
        productRepo.save(product);

        return Response.builder()
                .status(200)
                .message("Product created")
                .build();
    }

    @Override
    public Response updateProduct(Long productId, Long categoryId, MultipartFile image, String name, String description, BigDecimal price) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
        Category category = null;
        String productImageURl = null;


        if (categoryId != null) {
            category = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));

        }
        if (image != null) {
            productImageURl = awsS3Service.saveImageToS3(image);
        }
        if (category != null) product.setCategory(category);
        if (productImageURl != null) product.setImageURL(productImageURl);
        if (name != null) product.setName(name);
        if (price != null) product.setPrice(price);
        if (description != null) product.setDescription(description);

        productRepo.save(product);

        return Response.builder()
                .status(200)
                .message("Product updated")
                .build();
    }

    @Override
    public Response deleteProduct(Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
        productRepo.delete(product);

        return Response.builder()
                .status(200)
                .message("Product deleted")
                .build();
    }

    @Override
    public Response getProductById(Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
        ProductDto productDto = entityDtoMapper.mapProductToDtoBasic(product);

        return Response.builder()
                .status(200)
                .product(productDto)
                .build();
    }

    @Override
    public Response getAllProducts() {
        List<ProductDto> products = productRepo.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(entityDtoMapper::mapProductToDtoBasic).collect(Collectors.toList());
        return Response.builder()
                .status(200)
                .productList(products)
                .build();
    }

    @Override
    public Response getProductsByCategory(Long categoryId) {
        List<Product> products = productRepo.findByCategoryId(categoryId);
        if (products.isEmpty()) {
            throw new NotFoundException("No product found for this category");
        }
        List<ProductDto> productDtoList = products.stream()
                .map(entityDtoMapper::mapProductToDtoBasic).collect(Collectors.toList());
        return Response.builder()
                .status(200)
                .productList(productDtoList)
                .build();
    }

    @Override
    public Response searchProduct(String searchValue) {
        List<Product> products = productRepo.findByNameContainingOrDescriptionContaining(searchValue, searchValue);
        if (products.isEmpty()) {
            throw new NotFoundException("No product found for this search");
        }
        List<ProductDto> productDtoList = products.stream().map(entityDtoMapper::mapProductToDtoBasic).collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .productList(productDtoList)
                .build();
    }
}
