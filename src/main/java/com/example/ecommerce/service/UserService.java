package com.example.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ecommerce.modul.Users;
import com.example.ecommerce.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users saveUser(Users users){
        users.setRole("ROLE_USER");
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        return userRepo.save(users);
    }

    public boolean userexist(String email){
        return userRepo.existsByEmail(email);
    }
}
