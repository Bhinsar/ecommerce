package com.example.ecommerce.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
@Service
public class CommService {
    public void removeSessionMessage() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes()))
                .getRequest();
        HttpSession session = request.getSession();
        session.removeAttribute("Success");
        session.removeAttribute("Error");
    }
    // Send  Mail logic
    @Autowired
    private  JavaMailSender mailSender;

    public  boolean sendMail(String url, String email) throws UnsupportedEncodingException, MessagingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("bhinsarjagdish2003@gmail.com","ShoopingCart");
        helper.setTo(email);
        String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
				+ "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + url
				+ "\">Change my password</a></p>";
        helper.setSubject("Passoword Reset");
        helper.setText(content, true);
        mailSender.send(message);
        return true;
    }
    public String generateURl(HttpServletRequest request) {
        String siteurl = request.getRequestURL().toString();
        return siteurl.replace(request.getServletPath(),"");
    }
}
