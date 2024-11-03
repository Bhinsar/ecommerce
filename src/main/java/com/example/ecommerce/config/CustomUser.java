package com.example.ecommerce.config;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.ecommerce.modul.Users;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomUser implements UserDetails{
    
    private Users users;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(users.getRole());
        return Arrays.asList(authority);
    }

    @Override
    public String getPassword() {
        return users.getPassword();
    }

    @Override
    public String getUsername() {
        return users.getEmail();
    }
    @Override
    public boolean isAccountNonLocked() {
        return users.getAccountNonLocked();
    }
    @Override
    public boolean isEnabled() {
        return users.isEnable();
    }
}
