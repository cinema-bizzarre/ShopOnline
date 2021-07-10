package com.example.backend.dto;

import com.example.backend.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductDto {
    private Long id;

    @Size(min = 4, max = 255, message = "Title size: 4-255")
    private String title;

    @Min(value = 1, message = "Min price = 1")
    private BigDecimal price;

    @NotEmpty(message = "Category  may not be blank")
    private String categoryTitle;

    public ProductDto(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.categoryTitle = product.getCategory().getTitle();
    }
}