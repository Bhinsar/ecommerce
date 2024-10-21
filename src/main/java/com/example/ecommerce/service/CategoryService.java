package com.example.ecommerce.service;

import com.example.ecommerce.modul.Category;
import com.example.ecommerce.modul.Product;
import com.example.ecommerce.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
public class CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;


    public boolean exist(String categoryName) {
        return categoryRepo.existsByCategoryName(categoryName);
    }

    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }


    public void deleteCategory(int id) {
        categoryRepo.deleteById(id);
    }


    public Category saveCategory(Category category) throws IOException {
        return categoryRepo.save(category);
    }

    public Category getCategoryById(int categoryId) {
        return categoryRepo.findById(categoryId).orElse(new Category());
    }


}
