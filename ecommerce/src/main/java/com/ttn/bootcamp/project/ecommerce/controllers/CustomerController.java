package com.ttn.bootcamp.project.ecommerce.controllers;

import com.ttn.bootcamp.project.ecommerce.dtos.AddressDto;
import com.ttn.bootcamp.project.ecommerce.dtos.UserProfileDto;
import com.ttn.bootcamp.project.ecommerce.services.CustomerService;
import com.ttn.bootcamp.project.ecommerce.services.PasswordService;
import com.ttn.bootcamp.project.ecommerce.validators.EmailValidator;
import com.ttn.bootcamp.project.ecommerce.validators.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/user")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private PasswordValidator passwordValidator;

    @GetMapping(path = "/{userId}")
    public UserProfileDto getCustomerDetails(@PathVariable("userId") Long userId){
        return customerService.getCustomerProfile(userId);
    }

    @GetMapping(path = "/{userId}/address")
    public MappingJacksonValue getCustomerAddress(@PathVariable("userId") Long userId){
        return customerService.getCustomerAddress(userId);
    }

    @PutMapping(path = "/{userId}")
    public String updateCustomer(@Valid @RequestBody UserProfileDto userProfileDto, @PathVariable(value = "userId") Long userId){
        return customerService.updateCustomer(userProfileDto,userId);
    }

    @PatchMapping("/{userId}")
    public String updatePassword(@PathVariable(value = "userId") Long userId,
                                 @RequestParam String oldPass,@RequestParam String newPass,@RequestParam String confirmPass, HttpServletResponse response){
        if(passwordValidator.validatePassword(oldPass,newPass,confirmPass)){
            return passwordService.updatePassword(userId, oldPass, newPass, confirmPass);
        }else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Password must be matched or password must be of minimum 8 characters and maximum 15 characters and must contain 1 uppercase letter,1 lowercase letter,1 digit and 1 special character";
        }
    }

    @PostMapping(path = "/{userId}/address")
    public String addAddress(@Valid @RequestBody AddressDto addressDto, @PathVariable(value = "userId") Long userId){
       return customerService.addAddress(addressDto,userId);
    }

    //to check again
    @DeleteMapping(path = "/{addressId}")
    public String deleteAddress(@PathVariable(value = "addressId") Long addressId){
        return customerService.deleteAddress(addressId);
    }

    @DeleteMapping(path = "/{userId}/address/{addressId}")
    public String deleteAddress1(@PathVariable(value = "addressId") Long addressId){
        return customerService.deleteAddress(addressId);
    }

    @PutMapping(path = "/{userId}/address/{addressId}")
    public String updateAddress(@Valid @RequestBody AddressDto addressDto,@PathVariable(value = "addressId") Long addressId, @PathVariable(value = "userId") Long userId){
        return customerService.updateAddress(addressDto,addressId,userId);
    }

}
