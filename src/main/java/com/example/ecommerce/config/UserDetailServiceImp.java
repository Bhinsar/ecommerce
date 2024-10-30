package com.example.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.ecommerce.modul.Users;
import com.example.ecommerce.repository.UserRepo;

@Service
public class UserDetailServiceImp implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUser(user);
    }
    
}
