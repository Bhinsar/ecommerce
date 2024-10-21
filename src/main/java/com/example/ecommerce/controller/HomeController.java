package com.example.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    @GetMapping("/")
    public String index(){
        return "index";
    }@GetMapping("/login")
    public String login(){
        return "login&registration/login";
    }@GetMapping("/register")
    public String register(){
        return "login&registration/register";
    }
    @GetMapping("/products")
    public String allproducts(){
        return "allproduct";
    }
    @GetMapping("/view")
    public String viewproduct(){
        return "view";
    }
    @GetMapping("/cart")
    public String viewcart(){
        return "cart";
    }

}
