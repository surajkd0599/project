package com.ttn.bootcamp.project.ecommerce.controllers;

import com.ttn.bootcamp.project.ecommerce.dtos.*;
import com.ttn.bootcamp.project.ecommerce.models.Product;
import com.ttn.bootcamp.project.ecommerce.models.ProductVariation;
import com.ttn.bootcamp.project.ecommerce.repos.ProductVariationRepo;
import com.ttn.bootcamp.project.ecommerce.services.CategoryService;
import com.ttn.bootcamp.project.ecommerce.services.ProductService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/test")
public class TestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductVariationRepo productVariationRepo;

    @Autowired
    private CategoryService categoryService;


    ///////////////////////////////////////////////////
    @GetMapping(path = "/categories")
    public List<CategoryFieldValueDto> getCategories(){
        return categoryService.getCategories();
    }

    @PostMapping(path = "/product/{productId}")
    public String addVariation(@PathVariable(value = "productId") Long productId, @RequestBody ProductVariationDto productVariationDto){
        return productService.addProductVariation(productId, productVariationDto);
    }
    //////////////////////////////////////////////////////
    @GetMapping(path = "/categoryId/{categoryId}")
    public CategoryFieldValueDto getFieldValue(@PathVariable(value = "categoryId") Long categoryId){
        return categoryService.getFieldValueByCategoryId(categoryId);
    }
    ///////////////////////////////////////////////////////

    @GetMapping(path = "/variation")
    public List<ProductVariation> getAllVariation(){
        return productVariationRepo.findAll();
    }
    ///////////////////////////////////////////////
    @GetMapping(path = "{userId}/category/{categoryId}/product/{productId}/variation/{variationId}")
    public ProductVariationGetDto getProductVariation(@PathVariable(value = "userId") Long userId, @PathVariable(value ="variationId") Long variationId){
        return productService.getProductVariation(userId, variationId);
    }

    ////////////////////////////////////////////////////

    @GetMapping(path = "{userId}/category/{categoryId}/product/{productId}")
    public List<ProductVariationGetDto> getProductVariations(@PathVariable(value = "userId") Long userId, @PathVariable(value ="productId") Long productId){
        return productService.getProductVariations(userId, productId);
    }
//////////////////////////////////////////


    @PutMapping(path = "{userId}/category/{categoryId}/product/{productId}/name/variation/{variationId}")
    public String updateProductVariation(@PathVariable(value = "userId") Long userId, @PathVariable(value = "variationId") Long variationId,@RequestBody ProductVariationDto productVariationDto){
        return productService.updateProductVariation(userId,variationId,productVariationDto);
    }
    ///////////////////////////////////////////////////
    /////////////////////////////////////////////// Customer Data
    @GetMapping(path = "/getProduct/{categoryId}")
    public AllProductDto getProductsForUser(@PathVariable(value = "categoryId") Long categoryId){
        return productService.getAllProductsByCategoryId(categoryId);
    }

    @GetMapping(path = "/metadata/{variationId}")
    public JSONObject metadata(@PathVariable(value = "variationId") Long variationId){
        return productVariationRepo.findMetadata(variationId);
    }

    /////////////////////////////
    @GetMapping(path = "/category/{categoryId}")
    public CategoryFilterDto getFilteredCategories(@PathVariable(value = "categoryId") Long categoryId){
        return categoryService.categoryFilter(categoryId);
    }
    ///Similar product api left

    @GetMapping(path = "/similar/{productId}")
    public List<Product> getSimilarProducts(@PathVariable(value = "productId") Long productId){
        return productService.getSimilarProducts(productId);
    }
}
