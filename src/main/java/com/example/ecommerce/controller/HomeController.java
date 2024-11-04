package com.example.ecommerce.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.service.CommService;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommService commService;

    @Autowired
    private CartService cartService;

    @ModelAttribute
    public void getUserDetails(Principal p,Model m){
        List<Category> categories = categoryService.getAllCategories();
        m.addAttribute("categories", categories);
        if(p!=null){
            String email = p.getName();
            Users users = userService.getUserByEmail(email);
            Integer countCart = cartService.getCount(users.getId());
            m.addAttribute("userdetails", users);
            m.addAttribute("count",countCart);
        }
    }
    // home  page
    @GetMapping("/")
    public String index( Model model){
        List<Product> products = productService.getAllLatestProduct();
        
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
        modul.addAttribute("categoryProductsMap", categoryProductsMap);
        return "allproduct";
    }

    @GetMapping("/product/{category}")
    public String productByCategory(@PathVariable String category ,Model model){
        List<Product> products = productService.getProductsByCategory(category);
        model.addAttribute("products", products);
        return "category";
    }   

    @GetMapping("/view/{id}")
    public String viewproduct(@PathVariable Integer id, Model modul){
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
        if(userService.userexist(users.getEmail())){
            session.setAttribute("Error", "User Already Exist");
            return "redirect:/register";
        }
        String imgeName = file.isEmpty() ? "deafult.jpeg":file.getOriginalFilename();
        users.setProfileImageName(imgeName);
        Users users1 = userService.saveUser(users);
        if(!ObjectUtils.isEmpty(users1)){
            if(!file.isEmpty()){
                File saveFile = new File("D:/ecommerce/src/main/resources/static/img");
                // File saveFile = new ClassPathResource("static/img").getFile();
                
    
                 Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "user" + File.separator
                        + file.getOriginalFilename());            
                
    
                Files.write(path, file.getBytes());
            }
        }else{
            session.setAttribute("Error", "Something Wrong on the server");
            return "redirect:/register";
        }
        return  "redirect:/";

    }
    // Forgot Password controller
    @GetMapping("/forgot_password")
    public String showForgotPassword(){
        return "login&registration/forgot_password";
    }
    @PostMapping("/forgot_password")
    public String processForgotPassword(@RequestParam ("email") String email, HttpSession session,HttpServletRequest request) throws UnsupportedEncodingException, MessagingException{
        // Users user = userService.getUserByEmail(email);
        if(userService.userexist(email)){
            String resetToken = UUID.randomUUID().toString();
            userService.updateUserResetToken(email,resetToken);
            //Gernrate url
            String url = commService.generateURl(request)+"/reset_password?token="+resetToken;
            

            Boolean sendMail = commService.sendMail(url, email);

            if(sendMail){
                session.setAttribute("Success", "Check your Email");
            }else{
                session.setAttribute("Error", "Something Wrong on the server");
            }
        }else{
            session.setAttribute("Error", "Mail Not Found");
        }
        return "redirect:/forgot_password";
    }
    @GetMapping("/reset_password")
    public String showResetPassword(@RequestParam String token ,Model m){
        Users user = userService.getUserByResetToken(token);
        if(user==null){
            return "login&registration/error";
        }
        m.addAttribute("token", token);
        return "login&registration/reset_password";
    }

    @PostMapping("/reset_password")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("password") String password,@RequestParam("cpassword") String cpassword,HttpSession session){
        if (!password.equals(cpassword)) {
            session.setAttribute("Error", "Passwords do not match.");
            return "redirect:/reset_password?token=" + token;
        }
        Users user = userService.updatePassword(password ,token);
        if(user != null){
            session.setAttribute("Success","Password Successfully Updated");
        }else{
            session.setAttribute("Error", "Something Wrong on the server");
        }
        

        return "redirect:/reset_password?token="+token;
    }

}