package com.example.ecommerce.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.modul.Category;
import com.example.ecommerce.modul.Product;
import com.example.ecommerce.modul.Users;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.UserService;

import jakarta.servlet.http.HttpSession;


@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // home  page
    @GetMapping("/")
    public String index( Model model){
        List<Product> products = productService.getAllLatestProduct();
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("prod", products);
        return "index";
    }
    

    // product page
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

    @GetMapping("/product/{category}")
    public String productByCategory(@PathVariable String category ,Model model){
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        List<Product> products = productService.getProductsByCategory(category);
        model.addAttribute("products", products);
        return "category";
    }   

    @GetMapping("/view/{id}")
    public String viewproduct(@PathVariable long id, Model modul){
        List<Category> categories = categoryService.getAllCategories();
        modul.addAttribute("categories", categories);
        modul.addAttribute("product", productService.getProductById(id));
        return "view";
    }
    // login page
    @GetMapping("/signin")
    public String login(){
        return "login&registration/login";
    }
    // registration page
    @GetMapping("/register")
    public String register(){
        return "login&registration/register";
    }

    @PostMapping("/saveuser")
    public String saveUser(@ModelAttribute Users users,@RequestParam("cpassword") String cpassword, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        // chech that password and confrim password are same
        if(!users.getPassword().equals(cpassword)){
            session.setAttribute("Error", "Password and Confrim Password don't mach");
            return "redirect:/register";
        }
        // user mail check
        if(!userService.userexist(users.getEmail())){
            session.setAttribute("Error", "User Already Exist");
            return "redirect:/register";
        }
        String imgeName = file.isEmpty() ? "deafult.jpeg":file.getOriginalFilename();
        users.setProfileImageName(imgeName);
        Users users1 = userService.saveUser(users);
        if(!ObjectUtils.isEmpty(users1)){
            if(!file.isEmpty()){
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "user"+ File.separator + imgeName);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
        }else{
            session.setAttribute("Error", "Something Wrong on the server");
            return "redirect:/register";
        }
        return  "redirect:/";

    }

}