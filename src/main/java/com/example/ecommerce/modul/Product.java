package com.example.ecommerce.modul;

import java.sql.Blob;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String productName;
    private String productDescription;
    private double price;
    private String category;
    private int stock;
    private String imageName;
    private String imageType;
    @Lob
    private byte[] imageData;

}
