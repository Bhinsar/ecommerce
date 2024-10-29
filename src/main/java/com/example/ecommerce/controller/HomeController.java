package com.example.ecommerce.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.ecommerce.modul.Category;
import com.example.ecommerce.modul.Product;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.service.ProductService;


@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String index( Model model){
        List<Product> products = productService.getAllLatestProduct();
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("prod", products);
        return "index";
    }
    
    @GetMapping("/login")
    public String login(){
        return "login&registration/login";
    }
    
    @GetMapping("/register")
    public String register(){
        return "login&registration/register";
    }
    
    @GetMapping("/products")
    public String allproducts(Model modul) {
        List<Category> categories = categoryService.getAllCategories();
        Map<Category, List<Product>> categoryProductsMap = new LinkedHashMap<>(); // Use LinkedHashMap

        for (Category category : categories) {
            List<Product> productsByCategory = productService.getProductsByCategory(category.getCategoryName());
            List<Product> limitedProducts = productsByCategory.stream().limit(3).toList();  // Limit to 3 products per category
            categoryProductsMap.put(category, limitedProducts);
        }

        modul.addAttribute("categories", categories);
        modul.addAttribute("categoryProductsMap", categoryProductsMap);
        return "allproduct";
    }


    @GetMapping("/view/{id}")
    public String viewproduct(@PathVariable long id, Model modul){
        List<Category> categories = categoryService.getAllCategories();
        modul.addAttribute("categories", categories);
        modul.addAttribute("product", productService.getProductById(id));
        return "view";
    }
    @GetMapping("/product/{category}")
    public String productByCategory(@PathVariable String category ,Model model){
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        List<Product> products = productService.getProductsByCategory(category);
        model.addAttribute("products", products);
        return "category";
    }    

}
