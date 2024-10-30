package com.example.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.modul.Users;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {
    public Users findByEmail(String email);
    public boolean existsByEmail(String email);
} 
