package com.example.ecommerce.controller;

import com.example.ecommerce.modul.Category;
import com.example.ecommerce.modul.Product;

import jakarta.servlet.http.HttpSession;

import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.service.ProductService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;


    @GetMapping("/")
    public String index(){
        return "admin/index";
    }
    
    @GetMapping("/product")
    public String loadaddproduct(Model model){
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/addproduct";
    }

    // category controller

    @GetMapping("/category")
    public String listCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/category";
    }

    @PostMapping("/savecategory")
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
                               HttpSession session) throws IOException {

        String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
        category.setCategoryImageName(imageName);

        Boolean existCategory = categoryService.exist(category.getCategoryName());

        if (existCategory) {
            session.setAttribute("Error", "Category Name already exists");
        } else {

            Category saveCategory = categoryService.saveCategory(category);

            if (ObjectUtils.isEmpty(saveCategory)) {
                session.setAttribute("Error", "Not saved ! internal server error");
            } else {

                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category" + File.separator + file.getOriginalFilename());

                System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                session.setAttribute("Success", "Saved successfully");
            }
        }

        return "redirect:/admin/category";
    }

    @GetMapping("/delete/{id}")
    public String delteCategory(@PathVariable int id){
        categoryService.deleteCategory(id);
        return "redirect:/admin/category";
    }
    @GetMapping("/edit/{id}")
    public String loadEditCategory(@PathVariable int id) {

        return "admin/update";
    }


    // product controller

    @PostMapping("/saveproduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("imageFile") MultipartFile imageFile,HttpSession session) throws IOException{

        Product product2 = productService.saveProduct(product,imageFile);
        if(!ObjectUtils.isEmpty(product2)){
            session.setAttribute("Success", "Product Successfully Added");
        }else{
            session.setAttribute("Error","Not Save! Internal server Error");
        }

        return "redirect:/admin/product";
    }

}