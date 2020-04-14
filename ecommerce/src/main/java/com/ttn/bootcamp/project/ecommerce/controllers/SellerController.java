package com.ttn.bootcamp.project.ecommerce.controllers;

import com.ttn.bootcamp.project.ecommerce.services.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/ecommerce/seller/home")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @GetMapping(path = "/sellerProfile/{userId}")
    public MappingJacksonValue getSellerProfile(@PathVariable(value = "userId") Long userId){
        return sellerService.getSellerProfile(userId);
    }

    /*@Autowired
    ProductRepository productRepository;
    @Autowired
    private SellerService sellerService;

    @Autowired
    private ProductService productService;

    @PostMapping(path = "/addProduct")
    public String addProduct(@RequestBody ProductCategory productCategory) {
        return productService.addCategory(productCategory);
    }

    @GetMapping(path = "/getCategory")
    public List<Object[]> getCategory() {
        return productService.getCategory();
    }

    @GetMapping(path = "/getProduct/{categoryName}")
    public List<Object[]> getProduct(@PathVariable(value = "categoryName") String categoryName) {
        return productService.getProduct(categoryName);
    }

    @GetMapping(path = "/welcome")
    public String welcomePage(){
        return "Welcome to seller page";
    }*/
}
