package com.ttn.bootcamp.project.ecommerce.controllers;

import com.ttn.bootcamp.project.ecommerce.dtos.*;
import com.ttn.bootcamp.project.ecommerce.services.CategoryService;
import com.ttn.bootcamp.project.ecommerce.services.CustomerService;
import com.ttn.bootcamp.project.ecommerce.services.PasswordService;
import com.ttn.bootcamp.project.ecommerce.services.ProductService;
import com.ttn.bootcamp.project.ecommerce.validators.EmailValidator;
import com.ttn.bootcamp.project.ecommerce.validators.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

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

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

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

    @GetMapping(path = "/category")
    public List<CategoryFieldValueDto> getCategories(){
        return categoryService.getCategories();
    }

    @GetMapping(path = "/product/{productId}")
    public List<ProductVariationGetDto> getProduct(@PathVariable(value = "productId") Long productId){
        return productService.getProductForUser(productId);
    }

    @GetMapping(path = "{categoryId}/products")
    public AllProductDto getProducts(@PathVariable(value = "categoryId") Long categoryId){
        return productService.getAllProductsByCategoryId(categoryId);
    }

    @GetMapping(path = "/category/{categoryId}")
    public CategoryFilterDto getFilteredCategories(@PathVariable(value = "categoryId") Long categoryId){
        return categoryService.categoryFilter(categoryId);
    }
    ///Similar product api left


}
