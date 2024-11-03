package com.example.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.example.ecommerce.modul.Cart;
import com.example.ecommerce.modul.Product;
import com.example.ecommerce.modul.Users;
import com.example.ecommerce.repository.CartRepo;
import com.example.ecommerce.repository.ProductRepo;
import com.example.ecommerce.repository.UserRepo;

@Service
public class CartService {
    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired ProductRepo productRepo;


    public Cart saveCart(Integer pid , Integer uid){
        Users user =  userRepo.findById(uid).get();
        Product product = productRepo.findById(pid).get();
        Cart cartStatus = cartRepo.findByUserIdAndProductId(uid, pid);
        Cart cart = null;

		if (ObjectUtils.isEmpty(cartStatus)) {
			cart = new Cart();
			cart.setProduct(product);
			cart.setUser(user);
			cart.setQuantity(1);
			cart.setTotalPrice(1 * product.getFinalPrice());
            product.setStock(product.getStock() - 1);
		} else {
			cart = cartStatus;
			cart.setQuantity(cart.getQuantity() + 1);
			cart.setTotalPrice(cart.getQuantity() * cart.getProduct().getFinalPrice());
            product.setStock(product.getStock() - 1);
		}
        productRepo.save(product);
        return cartRepo.save(cart);

    }
    public List<Cart> getCartByUser(Integer uid){
        return null;
    }
}
