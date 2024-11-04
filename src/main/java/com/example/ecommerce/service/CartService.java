package com.example.ecommerce.service;

import java.util.ArrayList;
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
			cart.setTotalProductPrice(1 * product.getFinalPrice());
		} else {
			cart = cartStatus;
			cart.setQuantity(cart.getQuantity() + 1);
			cart.setTotalProductPrice(cart.getQuantity() * cart.getProduct().getFinalPrice());
		}
        return cartRepo.save(cart);

    }
    public List<Cart> getCartByUser(Integer uid){
        List<Cart> cartList = cartRepo.findByUserId(uid);
        Double totalOrderPrice =0.0;
        List<Cart> updateCarts =new ArrayList<>();
        for (Cart cart : cartList) {
            Double totalPrice = (cart.getProduct().getFinalPrice()*cart.getQuantity());
            cart.setTotalProductPrice(totalPrice);

            totalOrderPrice += totalPrice;
            cart.setTotalPrice(totalOrderPrice);
            updateCarts.add(cart); 
        }

        return updateCarts;
    }
    public Integer getCount(Integer uid){
        return cartRepo.countByUserId(uid);
       
    }
    public Cart updateCartQuantity(Integer cartId, Integer newQuantity) {
        Cart cart = cartRepo.findById(cartId).orElse(null);
    
        if (cart != null) {
            cart.setQuantity(newQuantity);            
            return cartRepo.save(cart);
        }
        return null;
    }
    public void deletecart(Integer cid) {
        cartRepo.deleteById(cid);
    }

}
