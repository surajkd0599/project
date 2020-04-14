package com.ttn.bootcamp.project.ecommerce.controllers;

import com.ttn.bootcamp.project.ecommerce.dtos.CustomerDto;
import com.ttn.bootcamp.project.ecommerce.dtos.SellerDto;
import com.ttn.bootcamp.project.ecommerce.models.Admin;
import com.ttn.bootcamp.project.ecommerce.models.User;
import com.ttn.bootcamp.project.ecommerce.services.AppUserDetailsService;
import com.ttn.bootcamp.project.ecommerce.services.CustomerActivateService;
import com.ttn.bootcamp.project.ecommerce.services.DtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/ecommerce/register")
public class AuthController {

    @Autowired
    private AppUserDetailsService appUserDetailsService;

    @Autowired
    private CustomerActivateService customerActivateService;

    @Autowired
    private DtoService dtoService;

    @PostMapping(path = "/customer")
    public String registerCustomer(@Valid @RequestBody CustomerDto customerDto,HttpServletResponse response){
        if(dtoService.validateCustomer(customerDto).equals("validated")) {
             response.setStatus(HttpServletResponse.SC_CREATED);
             return appUserDetailsService.registerCustomer(customerDto);

        }else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return dtoService.validateCustomer(customerDto);
        }
    }

    @PostMapping(path = "/seller")
    public String registerSeller(@Valid @RequestBody SellerDto sellerDto,HttpServletResponse response){
        if(dtoService.validateSeller(sellerDto).equals("validated")) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            String message = appUserDetailsService.registerSeller(sellerDto);
            if (!message.equals("Registration Successful")){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            return message;
        }else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return dtoService.validateSeller(sellerDto);
        }
    }

    @PutMapping(path = "/confirm-account")
    public String confirmCustomerAccount(@RequestParam("token") String token, HttpServletResponse response){
        String message = customerActivateService.activateCustomer(token);
        if(!message.equals("Successfully activated")){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return message;
    }

    @PostMapping(path = "/resend-activation")
    public String resendLink(@RequestParam("email") String email,HttpServletResponse response){
        String message = customerActivateService.resendLink(email);
        if(!message.equals("Successful")){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return message;
    }

    @PostMapping(path = "/admin")
    public User registerAdmin(@Valid @RequestBody Admin admin){

        User user = appUserDetailsService.registerAdmin(admin);

        return user;
    }
}
