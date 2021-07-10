package com.example.backend.dto;

import com.example.backend.entity.OrderItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderItemDto {

    private String productTitle;
    private Long productId; //added but not use
    @Min(value = 1, message = "Min quantity = 1")
    private int quantity;
    private BigDecimal pricePerProduct;
    private BigDecimal price;

    public OrderItemDto(OrderItem orderItem) {
        this.productId=orderItem.getProduct().getId();
        this.productTitle = orderItem.getProduct().getTitle();
        this.quantity = orderItem.getQuantity();
        this.pricePerProduct = orderItem.getPricePerProduct();
        this.price = orderItem.getPrice();
    }
}