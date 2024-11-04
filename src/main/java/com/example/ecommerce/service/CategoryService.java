package com.example.ecommerce.service;

import com.example.ecommerce.modul.Category;
import com.example.ecommerce.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


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


    public Category saveCategory(Category category){
        return categoryRepo.save(category);
    }


    public Category getCategoryById(int id){
        Category category = categoryRepo.findById(id).orElse(null);
        return category;
    }
}
