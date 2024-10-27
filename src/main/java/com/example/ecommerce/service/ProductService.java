package com.example.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.modul.Product;
import com.example.ecommerce.repository.ProductRepo;

@Service
public class ProductService {
    @Autowired
    private ProductRepo  productRepo;


    public Product saveProduct(Product product){       
       return productRepo.save(product);
    }


    public List<Product> getAllProduct() {
        return productRepo.findAll();
    }


    public void deleteProduct(long id) {
        productRepo.deleteById(id);
    }


    public Product getProductById(long id) {
        Product product = productRepo.findById(id).orElse(null);
        return product;
    }


    public double getDiscountPrice(double price, int discount) {
        
        double discountPrice = (discount / 100.0) * price;
        
        return price - discountPrice;
    }
    
}
