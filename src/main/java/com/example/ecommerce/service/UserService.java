package com.example.ecommerce.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ecommerce.modul.Users;
import com.example.ecommerce.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users saveUser(Users users){
        users.setRole("ROLE_USER");
        users.setEnable(true);
        users.setAccountNonLocked(true);
        users.setFailedAttempt(0);
        users.setResetToken(null);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        return userRepo.save(users);
    }

    public boolean userexist(String email){
        return userRepo.existsByEmail(email);
    }

    public Users getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public List<Users> getAllUser() {
        return userRepo.findAll();
    }

    public void updateAccountStatus(Boolean status, Integer id) {
        Users user = userRepo.findById(id).orElse(null);
        if (user != null) {
            user.setEnable(status);
            userRepo.save(user);
        }
    }

    public void increaseFailedAttempt(Users user){
        int attempt = user.getFailedAttempt()+1;
        user.setFailedAttempt(attempt);
        userRepo.save(user);

    }
    public void lockAccount(Users user){
        user.setAccountNonLocked(false);
        user.setLockTime(new Date() );
        userRepo.save(user);
    }
    public Boolean unlockAccountTimeExpired(Users user){
        long lockTime = user.getLockTime().getTime();
        long unLockTime = lockTime + 3000;
        // (1*60*60*1000);
        long currentTime = System.currentTimeMillis();
        if(unLockTime < currentTime){
            user.setAccountNonLocked(true);
            user.setFailedAttempt(0);
            user.setLockTime(null);
            userRepo.save(user);
            return true;
        }
        return false;
    }
    public void resetAttempt(Integer id){

    }

    public void updateUserResetToken(String email, String resetToken) {
        Users uesr = userRepo.findByEmail(email);
        uesr.setResetToken(resetToken);
        userRepo.save(uesr);
    }

    public Users getUserByResetToken(String token) {
        return userRepo.findByResetToken(token);
    }

    public Users updatePassword(String password, String token) {
        Users user = userRepo.findByResetToken(token);
        user.setPassword(passwordEncoder.encode(password));
        return userRepo.save(user);
    }
}
