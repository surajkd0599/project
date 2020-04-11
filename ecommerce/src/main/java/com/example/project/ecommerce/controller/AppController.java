package com.example.project.ecommerce.controller;

import com.example.project.ecommerce.services.AppUserDetailsService;
import com.example.project.ecommerce.services.PasswordService;
import com.example.project.ecommerce.validator.EmailValidation;
import com.example.project.ecommerce.validator.PasswordValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/ecommerce/app")
public class AppController {

    @Autowired
    private AppUserDetailsService appUserDetailsService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private EmailValidation emailValidation;

    @Autowired
    private PasswordValidation passwordValidation;

    @GetMapping("/doLogout")
    public String logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
        }
        return "Logged out successfully";
    }

    @PatchMapping("/update-password")
    public String updatePassword(@RequestParam("email") String email,
                                @RequestParam String oldPass, String newPass, String confirmPass, HttpServletResponse response){
        if(passwordValidation.validatePassword(oldPass,newPass,confirmPass)){
            String message = passwordService.updatePassword(email, oldPass, newPass, confirmPass);
            if(!message.equals("Password successfully changed")){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            return message;
        }else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Password must be matched or password must be of minimum 8 characters and maximum 15 characters and must contain 1 uppercase letter,1 lowercase letter,1 digit and 1 special character";
        }
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    /*@PutMapping("/updateUserDetails/{username}")
    public void updateUserUsername(@PathVariable("username") String username, String userName){
        appUserDetailsService.updateUserUsername(username,userName);
    }*/
}