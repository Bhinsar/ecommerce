package com.example.ecommerce.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ecommerce.modul.Cart;
import com.example.ecommerce.modul.Category;
import com.example.ecommerce.modul.Users;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @GetMapping("/")
    public String home(){
        return "user/home";
    }
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
    @GetMapping("/addCart")
    public String addToCart(@RequestParam("pid") Integer pid,@RequestParam("uid") Integer uid,HttpSession session){
        Cart saveCart = cartService.saveCart(pid, uid);
        if(saveCart == null){
            session.setAttribute("Error", "Product Add to Cart Failed");
        }else{
            session.setAttribute("Success", "Product Add to Cart Successfully");
        }
        return "redirect:/view/"+pid;
    }
    @GetMapping("/cart")
    public String showCartPage(Principal principal,Model model){
        String email = principal.getName();
        Users users = userService.getUserByEmail(email);
        List<Cart> cart = cartService.getCartByUser(users.getId());
        model.addAttribute("cart", cart);
        ;
        model.addAttribute("totalOderPrice", cart.get(cart.size()-1).getTotalPrice());
        return "user/cart";
    }
    @PostMapping("/cartQuantityUpdate")
    public String updateCartQuantity(
    @RequestParam("cartId") Integer cartId,
    @RequestParam("quantity") int quantity,
    HttpSession session) {
    Cart cart = cartService.updateCartQuantity(cartId,quantity);
    if (cart != null) {
        session.setAttribute("Error", "Unable to change quntity!! Server Error");        
    }

    return "redirect:/user/cart"; 
    }
    @GetMapping("/cartDelete")
    public String deletecart(@RequestParam("cid") Integer cid){
        cartService.deletecart(cid);
        return "redirect:/user/cart";
    }
}
