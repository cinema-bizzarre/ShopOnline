package com.example.backend.dto;

import com.example.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RegisteredUserDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;


    public User toUser() {
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        return user;
    }

    public static RegisteredUserDto fromUser(User user) {
        RegisteredUserDto registeredUserDto = new RegisteredUserDto();
        registeredUserDto.setUsername(user.getUsername());
        registeredUserDto.setFirstName(user.getFirstName());
        registeredUserDto.setLastName(user.getLastName());
        registeredUserDto.setEmail(user.getEmail());
        registeredUserDto.setPassword(user.getPassword());
        registeredUserDto.setPhoneNumber(user.getPhoneNumber());

        return registeredUserDto;
    }
}