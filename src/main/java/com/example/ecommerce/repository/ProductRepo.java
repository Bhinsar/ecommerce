package com.example.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.modul.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    
}
