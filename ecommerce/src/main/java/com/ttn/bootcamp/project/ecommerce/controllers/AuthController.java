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
@RequestMapping(path = "/register")
public class AuthController {

    @Autowired
    private AppUserDetailsService appUserDetailsService;

    @Autowired
    private CustomerActivateService customerActivateService;

    @Autowired
    private DtoService dtoService;

    @PostMapping(path = "/customer")
    public String registerCustomer(@Valid @RequestBody CustomerDto customerDto){
        dtoService.validateCustomer(customerDto);
        return appUserDetailsService.registerCustomer(customerDto);
    }

    @PostMapping(path = "/seller")
    public String registerSeller(@Valid @RequestBody SellerDto sellerDto){
        dtoService.validateSeller(sellerDto);
        return appUserDetailsService.registerSeller(sellerDto);
    }

    @PutMapping(path = "/confirm-account")
    public String confirmCustomerAccount(@RequestParam("token") String token){
        return customerActivateService.activateCustomer(token);
    }

    @PostMapping(path = "/resend-activation")
    public String resendLink(@RequestParam("email") String email){
        return customerActivateService.resendLink(email);
    }

    @PostMapping(path = "/admin")
    public User registerAdmin(@Valid @RequestBody Admin admin){

        User user = appUserDetailsService.registerAdmin(admin);

        return user;
    }
}
