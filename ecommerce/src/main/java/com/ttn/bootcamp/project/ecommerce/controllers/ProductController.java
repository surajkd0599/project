package com.ttn.bootcamp.project.ecommerce.controllers;

import com.ttn.bootcamp.project.ecommerce.models.ProductVariation;
import com.ttn.bootcamp.project.ecommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    //@PreAuthorize("hasRole('SELLER')")
    /*@GetMapping(path = "/getCategory")
    public List<Object[]> getCategry() {
        return productService.getCategory();
    }*//*

   *//* @GetMapping(path = "/getProduct/{categoryName}")
    public List<Object[]> getProduct(@PathVariable(value = "categoryName") String categoryName) {
        return productService.getProduct(categoryName);
    }
*//*
    @GetMapping(path = "/getVariation/{productName}")
    public List<Object[]> getVariation(@PathVariable(value = "productName") String productName) {
        return productService.getVariation(productName);
    }

    @GetMapping(path = "/getVariation/priceLowToHigh/{productName}")
    public List<Object[]> priceLowToHigh(@PathVariable(value = "productName") String productName) {
        return productService.priceLowToHigh(productName);
    }

    @GetMapping(path = "/getVariation/priceHighToLow/{productName}")
    public List<Object[]> priceHighToLow(@PathVariable(value = "productName") String productName) {
        return productService.priceHighToLow(productName);
    }

    @PutMapping(path = "/updateStockInProduct/{productName}" )
    public void updateStockByAdmin(@PathVariable(value = "productName")String productName,@RequestBody ProductVariation productVariation)
            {

    }*/
}
