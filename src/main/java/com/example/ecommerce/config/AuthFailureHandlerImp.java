package com.example.ecommerce.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.example.ecommerce.modul.Users;
import com.example.ecommerce.repository.UserRepo;
import com.example.ecommerce.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailureHandlerImp extends SimpleUrlAuthenticationFailureHandler{

    @Autowired
    public UserRepo userRepo;

    @Autowired
    public UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,HttpServletResponse response, AuthenticationException exception)throws IOException,ServletException{
        String email = request.getParameter("username");
        Users user =  userRepo.findByEmail(email);

        if(user.isEnable()){
            if(user.getAccountNonLocked()){
                if(user.getFailedAttempt()<10){
                    userService.increaseFailedAttempt(user);
                    int attempt = user.getFailedAttempt(); 
                    exception = new LockedException("You have " + (11-attempt) + " attempts left");

                }else{
                    userService.lockAccount(user);
                    exception =new LockedException("Your account is Locked");
                }
            }else{
                if (userService.unlockAccountTimeExpired(user)) {
					exception = new LockedException("Your account is unlocked !! Please try to login");
				} else {
					exception = new LockedException("your account is Locked !! Please try after sometimes");
				}
            }

        }else{
            exception = new LockedException("Your account is inactive");
        }

        super.setDefaultFailureUrl("/signin?error");
        super.onAuthenticationFailure(request, response, exception);
    }
        
}
