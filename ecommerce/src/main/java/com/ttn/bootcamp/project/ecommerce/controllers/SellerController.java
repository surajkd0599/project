package com.ttn.bootcamp.project.ecommerce.controllers;

import com.ttn.bootcamp.project.ecommerce.dtos.AddressDto;
import com.ttn.bootcamp.project.ecommerce.dtos.ProductDto;
import com.ttn.bootcamp.project.ecommerce.dtos.ProductViewDto;
import com.ttn.bootcamp.project.ecommerce.dtos.SellerProfileDto;
import com.ttn.bootcamp.project.ecommerce.models.CategoryMetaDataFieldValues;
import com.ttn.bootcamp.project.ecommerce.services.CategoryService;
import com.ttn.bootcamp.project.ecommerce.services.PasswordService;
import com.ttn.bootcamp.project.ecommerce.services.ProductService;
import com.ttn.bootcamp.project.ecommerce.services.SellerService;
import com.ttn.bootcamp.project.ecommerce.validators.EmailValidator;
import com.ttn.bootcamp.project.ecommerce.validators.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/seller")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private PasswordValidator passwordValidator;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping(path = "/{userId}")
    public MappingJacksonValue getSellerProfile(@PathVariable(value = "userId") Long userId){
        return sellerService.getSellerProfile(userId);
    }

    @PutMapping(path = "/{userId}" )
    public String updateSeller(@Valid @RequestBody SellerProfileDto sellerProfileDto, @PathVariable(value = "userId") Long userId){
        return sellerService.updateSeller(sellerProfileDto,userId);
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

    @PutMapping(path = "/{userId}/address/{addressId}")
    public String updateAddress(@Valid @RequestBody AddressDto addressDto, @PathVariable(value = "addressId") Long addressId,@PathVariable Long userId){
        return sellerService.updateAddress(addressDto,addressId,userId);
    }

    @GetMapping(path = "/categories")
    public List<CategoryMetaDataFieldValues> getCategories(){
        return categoryService.getCategories();
    }

    @PostMapping(path = "{userId}/category/{categoryId}")
    public String addProduct(@PathVariable(value = "userId") Long userId, @PathVariable(value = "categoryId") Long categoryId, @Valid @RequestBody ProductDto productDto){
        return productService.addProduct(userId,categoryId,productDto);
    }

    @GetMapping(path = "{userId}/product/{productId}")
    public ProductViewDto viewProduct(@PathVariable(value = "userId") Long userId, @PathVariable(value = "productId") Long productId){
        return productService.viewProduct(userId, productId);
    }

    @GetMapping(path = "{userId}/product")
    public Set<ProductViewDto> viewProducts(@PathVariable(value = "userId") Long userId){
        return productService.getProducts(userId);
    }
}
