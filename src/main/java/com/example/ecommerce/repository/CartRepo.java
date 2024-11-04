package com.example.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.modul.Cart;

@Repository
public interface CartRepo extends JpaRepository<Cart , Integer> {
    public Cart findByUserIdAndProductId(Integer userId,Integer productId);
    public List<Cart> findByUserId(Integer userId);

    public Integer countByUserId(Integer uid);
} 