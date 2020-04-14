package com.ttn.bootcamp.project.ecommerce.controllers;

import com.ttn.bootcamp.project.ecommerce.dtos.AddressDto;
import com.ttn.bootcamp.project.ecommerce.dtos.UserProfile;
import com.ttn.bootcamp.project.ecommerce.repos.AddressRepo;
import com.ttn.bootcamp.project.ecommerce.repos.CustomerRepository;
import com.ttn.bootcamp.project.ecommerce.services.CustomerService;
import com.ttn.bootcamp.project.ecommerce.services.DtoService;
import com.ttn.bootcamp.project.ecommerce.services.PasswordService;
import com.ttn.bootcamp.project.ecommerce.services.ProductService;
import com.ttn.bootcamp.project.ecommerce.validators.EmailValidator;
import com.ttn.bootcamp.project.ecommerce.validators.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/ecommerce/user/home")
public class CustomerController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DtoService dtoService;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private PasswordValidator passwordValidator;

    @GetMapping(path = "/getDetails/{userId}")
    public UserProfile getCustomerDetails(@PathVariable("userId") Long userId){
        return customerService.getCustomerProfile(userId);
    }

    @GetMapping(path = "/getAddress/{userId}")
    public MappingJacksonValue getCustomerAddress(@PathVariable("userId") Long userId){
        return customerService.getCustomerAddress(userId);
    }

    @PutMapping(path = "/updateProfile/{userId}")
    public String updateCustomer(@Valid @RequestBody UserProfile userProfile, @PathVariable(value = "userId") Long userId, HttpServletResponse response){
        String message = customerService.updateCustomer(userProfile,userId);
        if (!message.equals("User updated")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return message;
    }

    @PatchMapping("/updatePassword/{userId}")
    public String updatePassword(@PathVariable(value = "userId") Long userId,
                                 @RequestParam String oldPass,@RequestParam String newPass,@RequestParam String confirmPass, HttpServletResponse response){
        if(passwordValidator.validatePassword(oldPass,newPass,confirmPass)){
            String message = passwordService.updatePassword(userId, oldPass, newPass, confirmPass);
            if(!message.equals("Password successfully changed")){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            return message;
        }else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Password must be matched or password must be of minimum 8 characters and maximum 15 characters and must contain 1 uppercase letter,1 lowercase letter,1 digit and 1 special character";
        }
    }

    @PostMapping(path = "/addAddress/{userId}")
    public String addAddress(@Valid @RequestBody AddressDto addressDto, @PathVariable(value = "userId") Long userId, HttpServletResponse response){
       String message = customerService.addAddress(addressDto,userId);
       if(!message.equals("Address added")){
           response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
       }
       return message;
    }

    @DeleteMapping(path = "/deleteAddress/{addressId}")
    public String deleteAddress(@PathVariable(value = "addressId") Long addressId, HttpServletResponse response){
        String message = customerService.deleteAddress(addressId);
        if (!message.equals("Address deleted")){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return message;
    }

    @PutMapping(path = "/updateAddress/{addressId}")
    public String updateAddress(@Valid @RequestBody AddressDto addressDto,@PathVariable(value = "addressId") Long addressId, HttpServletResponse response){
        String message = customerService.updateAddress(addressDto,addressId);
        if(!message.equals("Address Updated")){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return message;
    }

}
