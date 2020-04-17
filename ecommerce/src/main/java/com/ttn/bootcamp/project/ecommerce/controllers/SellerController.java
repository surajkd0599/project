package com.ttn.bootcamp.project.ecommerce.controllers;

import com.ttn.bootcamp.project.ecommerce.dtos.*;
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

    /////////// Product started
    @GetMapping(path = "/categories")
    public List<CategoryFieldValueDto> getCategories(){
        return categoryService.getCategories();
    }

    @PostMapping(path = "{userId}/category/{categoryId}/product")
    public String addProduct(@PathVariable(value = "userId") Long userId, @PathVariable(value = "categoryId") Long categoryId, @Valid @RequestBody ProductDto productDto){
        return productService.addProduct(userId,categoryId,productDto);
    }

    @PostMapping(path = "/product/{productId}")
    public String addVariation(@PathVariable(value = "productId") Long productId, @RequestBody ProductVariationDto productVariationDto){
        return productService.addProductVariation(productId, productVariationDto);
    }

    @GetMapping(path = "{userId}/category/{categoryId}/product/{productId}")
    public ProductViewDto viewProduct(@PathVariable(value = "userId") Long userId, @PathVariable(value = "productId") Long productId){
        return productService.getProduct(userId, productId);
    }

    @GetMapping(path = "{userId}/category/{categoryId}/product/{productId}/variation/{variationId}")
    public ProductVariationGetDto getProductVariation(@PathVariable(value = "userId") Long userId, @PathVariable(value ="variationId") Long variationId){
        return productService.getProductVariation(userId, variationId);
    }

    @GetMapping(path = "{userId}/category/{categoryId}/product/{productId1}")
    public List<ProductVariationGetDto> getProductVariations(@PathVariable(value = "userId") Long userId, @PathVariable(value ="productId1") Long productId1){
        return productService.getProductVariations(userId, productId1);
    }

    @GetMapping(path = "{userId}/category/{categoryId}/product")
    public Set<ProductViewDto> viewProducts(@PathVariable(value = "userId") Long userId){
        return productService.getProducts(userId);
    }

    @DeleteMapping(path = "{userId}/category/{categoryId}/product/{productId}")
    public String deleteProduct(@PathVariable(value = "userId") Long userId, @PathVariable(value = "productId") Long productId){
        return productService.deleteProduct(userId,productId);
    }

    @PutMapping(path = "{userId}/category/{categoryId}/product/{productId}")
    public String updateProduct(@PathVariable(value = "userId") Long userId ,@PathVariable(value = "productId") Long productId, @RequestBody ProductViewDto productViewDto){
        return productService.updateProduct(userId, productId, productViewDto);
    }

    @PutMapping(path = "{userId}/category/{categoryId}/product/{productId}/name")
    public String updateProductName(@PathVariable(value = "userId") Long userId,@PathVariable(value = "categoryId") Long categoryId,@PathVariable(value = "productId") Long productId, @RequestBody ProductViewDto productViewDto){
        return productService.updateProductName(userId, categoryId, productId, productViewDto);
    }

    @PutMapping(path = "{userId}/category/{categoryId}/product/{productId}/name/variation/{variationId}")
    public String updateProductVariation(@PathVariable(value = "userId") Long userId, @PathVariable(value = "variationId") Long variationId,@RequestBody ProductVariationDto productVariationDto){
        return productService.updateProductVariation(userId,variationId,productVariationDto);
    }
}
