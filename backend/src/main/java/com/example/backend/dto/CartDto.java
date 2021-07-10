package com.example.backend.dto;

import com.example.backend.util.Cart;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class CartDto {

    private List<OrderItemDto> items;
    private BigDecimal sum;
    private int allItems;

    public CartDto(Cart cart) {
        this.items = cart.getItems().stream().map(OrderItemDto::new).collect(Collectors.toList());
        this.sum = cart.getSum();
        this.allItems=cart.getAllItems();
    }

}