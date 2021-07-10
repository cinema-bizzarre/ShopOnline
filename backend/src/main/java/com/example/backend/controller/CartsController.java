package com.example.backend.controller;

import com.example.backend.dto.CartDto;
import com.example.backend.util.Cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Slf4j
public class CartsController {
    private final Cart cart;


    @GetMapping()
    public CartDto getCart() {
        return new CartDto(cart);
    }


    @GetMapping("/add/{productId}")
    public void addToCart(@PathVariable(name = "productId") Long id) {
        cart.addToCart(id);
    }

    @GetMapping("/quantity")
    public void addToCart(@RequestParam String title, @RequestParam String incDec) {
        cart.quantity(title, incDec);
    }


    @GetMapping("/clear")
    public void clearCart() {
        cart.clear();
    }

    @GetMapping("/delete")
    public void deleteProductFromCart(@RequestParam String title) {
        log.info("delete from cart product with title=" + title);
        cart.deleteItem(title);
    }
}

