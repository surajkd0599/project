package com.example.project.ecommerce.controller;

import com.example.project.ecommerce.model.ProductCategory;
import com.example.project.ecommerce.repos.ProductRepository;
import com.example.project.ecommerce.services.ProductService;
import com.example.project.ecommerce.services.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/ecommerce/seller/home")
public class SellerController {

    @Autowired
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
    }
}
