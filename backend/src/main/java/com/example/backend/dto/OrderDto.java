package com.example.backend.dto;

import com.example.backend.entity.Order;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class OrderDto {

    private Long id;
    private String description;
    private BigDecimal price;

    public OrderDto(Order order) {
        this.id=order.getId();
        this.price=order.getOrderSum();
        this.description=order.getItems().stream().map(orderItem -> orderItem.getProduct().getTitle() + " x" + orderItem.getQuantity()).collect(Collectors.joining(", "));

    }
}