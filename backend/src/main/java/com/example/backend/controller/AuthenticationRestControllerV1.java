package com.example.backend.controller;

import com.example.backend.dto.AuthenticationRequestDto;
import com.example.backend.dto.RegisteredUserDto;
import com.example.backend.entity.User;
import com.example.backend.security.jwt.JwtTokenProvider;
import com.example.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping(value = "/api/v1/auth/")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationRestControllerV1 {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

      @Autowired
    public AuthenticationRestControllerV1(AuthenticationManager authenticationManager,
                                          JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
            }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                    requestDto.getPassword()));
            Optional<User> user = userService.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }


            String token = jwtTokenProvider.createToken();
            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity activate(@PathVariable String code) {
        boolean isActivated = userService.activateUser(code);
        if (isActivated) {
            Map<Object, Object> response = new HashMap<>();
            response.put("status", HttpStatus.OK);
            return ResponseEntity.ok(response);
        } else {
            return new ResponseEntity<>("User already activated", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("register")
    public ResponseEntity register(@RequestBody RegisteredUserDto registeredUserDto) {
        Optional<User> user = userService.findByUsername(registeredUserDto.getUsername());
        if (user == null) {
            userService.register(registeredUserDto.toUser());
            Map<Object, Object> response = new HashMap<>();
            response.put("status", HttpStatus.OK);
            return ResponseEntity.ok(response);
        } else {
            return new ResponseEntity<>("Username or email already exist", HttpStatus.BAD_REQUEST);
        }
    }
}