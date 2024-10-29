package com.example.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.modul.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    List<Product> findByCategory(String category);
    
    @Query("SELECT p FROM Product p ORDER BY p.id DESC")
    List<Product> findAllLatestProducts();
}
