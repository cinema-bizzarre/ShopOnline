package com.example.backend.service;

import com.example.backend.dto.ProductDto;
import com.example.backend.entity.Category;
import com.example.backend.entity.Product;
import com.example.backend.error.ResourceNotFoundException;
import com.example.backend.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;


    public Page<Product> findPage(int page, int pageSize) {
        return productRepository.findAllBy(PageRequest.of(page, pageSize));
    }

    public Optional<Product> findProductByID(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = findProductByID(productDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Product doesn't exist with id = " + productDto.getId() + " (Process: update product.)"));
        product.setPrice(productDto.getPrice());
        product.setTitle(productDto.getTitle());
        Category category = categoryService.findByTitle(productDto.getCategoryTitle()).orElseThrow(() ->
                new ResourceNotFoundException("Category doesn't exist product.CategoryTitle = " + productDto.getCategoryTitle() + " (Process: update product.)"));
        product.setCategory(category);
        return new ProductDto(product);
    }


    public boolean deleteProductByID(Long id) {
        productRepository.delete(findProductByID(id).get());
        return productRepository.findById(id).isEmpty() ? true : false;
    }

    @Transactional
    public ProductDto createNewProduct(ProductDto productDto) {
        Product product = new Product();
        product.setPrice(productDto.getPrice());
        product.setTitle(productDto.getTitle());
        Category category = categoryService.findByTitle(productDto.getCategoryTitle()).orElseThrow(() ->
                new ResourceNotFoundException("Category doesn't exist product.CategoryTitle = " + productDto.getCategoryTitle() + " (Process: create new product.)"));
        product.setCategory(category);
        productRepository.save(product);
        return new ProductDto(product);
    }
}