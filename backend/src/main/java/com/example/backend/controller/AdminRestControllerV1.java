package com.example.backend.controller;

import com.example.backend.dto.AdminUserDto;
import com.example.backend.entity.Product;
import com.example.backend.entity.User;
import com.example.backend.service.ProductService;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/admin/")
public class AdminRestControllerV1 {

    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public AdminRestControllerV1(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService =  productService;
    }

    @GetMapping(value = "user/all")
    public ResponseEntity<List<AdminUserDto>> getAllUsers() {
        List<User> users = userService.getAll();
        List<AdminUserDto> result = new ArrayList<>();

        if (users == null || users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        users.forEach(user -> result.add(AdminUserDto.fromUser(user)));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "user/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id) {
        User user = userService.findById(id);
        if (user != null) {
            userService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    @DeleteMapping(value = "product/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") Long id){
        Optional<Product> product = productService.findProductByID(id);
        if (product != null) {
            productService.deleteProductByID(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
