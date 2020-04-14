package com.ttn.bootcamp.project.ecommerce.controllers;

import com.ttn.bootcamp.project.ecommerce.services.ForgotPasswordService;
import com.ttn.bootcamp.project.ecommerce.validators.EmailValidator;
import com.ttn.bootcamp.project.ecommerce.validators.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/ecommerce/forgotPassword")
public class PasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private PasswordValidator passwordValidator;

    @PostMapping(path = "/token/{email}")
    public String sendToken(@PathVariable("email") String email, HttpServletResponse response){
        if (emailValidator.validateEmail(email)){
            String message = forgotPasswordService.sendToken(email);
            if(!message.equals("Change your password")){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            return message;
        }else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Email is not valid";
        }
    }

    @PatchMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("email") String email, @RequestParam String pass, @RequestParam String cpass,HttpServletResponse response) {
        if(passwordValidator.validatePassword(pass,cpass)){
            String message = forgotPasswordService.resetPassword(email, token, pass, cpass);
            if(!message.equals("Password successfully changed")){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            return message;
        }else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Password must be matched or password must be of minimum 8 characters and maximum 15 characters and must contain 1 uppercase letter,1 lowercase letter,1 digit and 1 special character";
        }
    }
}
