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
    public String index() {
        return "admin/index";
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

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category" + File.separator
                        + file.getOriginalFilename());

                System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                session.setAttribute("Success", "Saved successfully");
            }
        }

        return "redirect:/admin/category";
    }

    @GetMapping("/delete/{id}")
    public String delteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/category";
    }

    @GetMapping("/edit/{id}")
    public String loadEditCategory(@PathVariable int id, Model model) {
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "admin/editcategory";
    }

    @PostMapping("/update")
    public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file){
        Category oldCategory = categoryService.getCategoryById(category.getId());
        if (!ObjectUtils.isEmpty(oldCategory)) {
            oldCategory.setCategoryName(category.getCategoryName());
            String imageName = file.isEmpty() ? oldCategory.getCategoryImageName() : file.getOriginalFilename();
            // System.out.println(imageName);
            oldCategory.setCategoryImageName(imageName);
        }

        categoryService.saveCategory(oldCategory);
        return "redirect:/admin/category";
    }

    // product controller
    @GetMapping("/product")
    public String loadaddproduct(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/addproduct";
    }
    @PostMapping("/saveproduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("imageFile") MultipartFile imageFile,
            HttpSession session) throws IOException {

        String imageName = imageFile != null ? imageFile.getOriginalFilename() : "default.jpg";
        product.setProductImageName(imageName);
        product.setDiscount(0);
        product.setFinalPrice(product.getPrice());

        Product saveCategory = productService.saveProduct(product);

        if (ObjectUtils.isEmpty(saveCategory)) {
            session.setAttribute("Error", "Not saved ! internal server error");
        } else {

            File saveFile = new ClassPathResource("static/img").getFile();

            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product" + File.separator
                    + imageFile.getOriginalFilename());

            System.out.println(path);
            Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            session.setAttribute("Success", "Saved successfully");
        }

        return "redirect:/admin/product";
    }
    // view product controller
    @GetMapping("/allproduct")
    public String viewProduct(Model model) {
        model.addAttribute("products", productService.getAllProduct());
        return "admin/products";
    }
    @GetMapping("/deleteproduct/{id}")
    public String deleteProduct(@PathVariable("id") long id) {

        productService.deleteProduct(id);

        return "redirect:/admin/allproduct";

    }
    @GetMapping("/editproduct/{id}")
    public String editProduct(@PathVariable("id") long id, Model model) {
        model.addAttribute("product",productService.getProductById(id));
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/editproduct";
    }

    @PostMapping("/updateproduct")
    public String updateProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile file){
        Product oldProduct = productService.getProductById(product.getId());
        
        if(!ObjectUtils.isEmpty(oldProduct)){
            oldProduct.setProductName(product.getProductName());
            oldProduct.setPrice(product.getPrice());
            oldProduct.setDiscount(product.getDiscount());
            oldProduct.setFinalPrice(productService.getDiscountPrice(product.getPrice(),product.getDiscount()));
            oldProduct.setStock(product.getStock());
            oldProduct.setProductDescription(product.getProductDescription());
            oldProduct.setCategory(product.getCategory());
            String imageName = file.isEmpty() ? oldProduct.getProductImageName() : file.getOriginalFilename();
            oldProduct.setProductImageName(imageName);
        }else{
            System.out.println("awetet");
        }

        productService.saveProduct(oldProduct);


        return "redirect:/admin/allproduct";
    }
}