package com.example.backend.security.jwt;

import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(Optional<User> user) {
        return new JwtUser(
                user.get().getId(),
                user.get().getUsername(),
                user.get().getEmail(),
                user.get().getPassword(),
                mapToGrantedAuthorities(new ArrayList<>(user.get().getRoles()))
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> userRoles) {
        return userRoles.stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getName())
                ).collect(Collectors.toList());
    }
}
