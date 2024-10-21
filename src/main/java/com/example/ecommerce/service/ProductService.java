package com.example.ecommerce.service;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.modul.Product;
import com.example.ecommerce.repository.ProductRepo;

@Service
public class ProductService {
    @Autowired
    private ProductRepo  productRepo;


    public Product saveProduct(Product product, MultipartFile imageFile) throws IOException {
        
       product.setImageName(imageFile.getOriginalFilename());
       product.setImageType(imageFile.getContentType());
       product.setImageData(imageFile.getBytes());
       
       return productRepo.save(product);

    }
    
}
