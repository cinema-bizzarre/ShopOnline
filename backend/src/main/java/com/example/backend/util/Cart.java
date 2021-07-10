package com.example.backend.util;

import com.example.backend.entity.OrderItem;
import com.example.backend.entity.Product;
import com.example.backend.error.ResourceNotFoundException;
import com.example.backend.service.ProductService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Component
@RequiredArgsConstructor
public class Cart {
    private final ProductService productService;
    private List<OrderItem> items;
    private BigDecimal sum;
    private int allItems;

    public void addToCart(Long id) {
        for (OrderItem orderItem : items) {
            if (orderItem.getProduct().getId().equals(id)) {
                orderItem.incrementQuantity();
                recalculate();
                return;
            }
        }
        Product product = productService.findProductByID(id).orElseThrow(() ->
                new ResourceNotFoundException("Product doesn't exist with id: " + id + ". " +
                        "An error occurred while executing the procedure: adding an item to the cart. "));
        items.add(new OrderItem(product));
        recalculate();
    }

    private void recalculate() {
        sum = BigDecimal.ZERO;
        allItems = 0;
        for (OrderItem oi : items) {
            sum = sum.add(oi.getPrice());
            allItems += oi.getQuantity();
        }
    }

    public void deleteItem(String title) {
        sum = BigDecimal.ZERO;
        items.removeIf(oi -> oi.getProduct().getTitle().equals(String.valueOf(title)));
        recalculate();
    }

    public void clear() {
        items.clear();
        recalculate();
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }


    @PostConstruct
    public void init() {
        items = new ArrayList<>();
    }

    public void quantity(String title, String incDec) {

        for (OrderItem orderItem : items) {
            if (incDec.equals("+") && orderItem.getProduct().getTitle().equals(title)) {
                orderItem.incrementQuantity();
                recalculate();
                return;

            } else if (incDec.equals("-") && orderItem.getProduct().getTitle().equals(title)) {
                if (orderItem.getQuantity() <= 1) return;
                orderItem.decrementQuantity();
                recalculate();
                return;
            }
        }
    }
}