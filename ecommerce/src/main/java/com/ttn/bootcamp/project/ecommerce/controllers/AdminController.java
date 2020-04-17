package com.ttn.bootcamp.project.ecommerce.controllers;

import com.ttn.bootcamp.project.ecommerce.dtos.*;
import com.ttn.bootcamp.project.ecommerce.models.ProductVariation;
import com.ttn.bootcamp.project.ecommerce.repos.ProductVariationRepo;
import com.ttn.bootcamp.project.ecommerce.services.AdminService;
import com.ttn.bootcamp.project.ecommerce.services.CategoryService;
import com.ttn.bootcamp.project.ecommerce.services.ProductService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductVariationRepo productVariationRepo;

    @GetMapping(path = "/customers")
    public MappingJacksonValue getCustomers(@RequestParam(defaultValue = "0") String page, @RequestParam(defaultValue = "10")String size, @RequestParam(defaultValue = "id") String SortBy){
        return adminService.registeredCustomers(page, size, SortBy);
    }

    @GetMapping(path = "/sellers")
    public MappingJacksonValue getSellers(@RequestParam(defaultValue = "0") String page, @RequestParam(defaultValue = "10")String size, @RequestParam(defaultValue = "id") String SortBy){
        return adminService.registeredSellers(page, size, SortBy);
    }

    @PatchMapping(path = "/activateCustomer/{id}")
    public String activateCustomer(@PathVariable(value = "id") Long id){
        return adminService.activateUser(id);
    }

    @PatchMapping(path = "/deActivateCustomer/{id}")
    public String deactivateCustomer(@PathVariable(value = "id") Long id){
        return adminService.deactivateUser(id);
    }

    @PatchMapping(path = "/activateSeller/{id}")
    public String activateSeller(@PathVariable(value = "id") Long id){
        return adminService.activateUser(id);
    }

    @PatchMapping(path = "/deActivateSeller/{id}")
    public String deactivateSeller(@PathVariable(value = "id") Long id){
        return adminService.deactivateUser(id);
    }

    @PostMapping(path = "/metaDataField")
    public String addMetaDataField(@Valid @RequestBody MetaDataFieldDto metaDataFieldDto){
        return categoryService.addMetaDataField(metaDataFieldDto);
    }

    @GetMapping(path = "/metaData")
    public MappingJacksonValue getMetaDataField(){
        return categoryService.getMetaDataFields();
    }

    @PostMapping(path = "/category")
    public String addCategory(@RequestBody ProductCategoryDto productCategoryDto, HttpServletResponse response){

        String message = categoryService.addCategory(productCategoryDto);

        if(message.equals("Category name already exist")){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return message;
    }

    @GetMapping(path = "/category/{categoryId}")
    public List<ProductCategoryDto> getCategoryById(@PathVariable(value = "categoryId")  Long categoryId){
        return categoryService.getCategoryById(categoryId);
    }

    @GetMapping(path = "/category")
    public Set<Set<ProductCategoryDto>> getCategory(){
        return categoryService.getCategory();
    }

    @PutMapping(path = "/update/{categoryId}")
    public String updateCategory(@PathVariable(value = "categoryId") Long categoryId, @RequestBody ProductCategoryDto productCategoryDto){
        return categoryService.updateCategory(categoryId, productCategoryDto);
    }

    @PostMapping(path = "/value/{categoryId}")
    public String addValue(@PathVariable(value = "categoryId") Long categoryId, @RequestBody List<MetaDataFieldValueDto> metaDataFieldValueDtos){
        return categoryService.addValue(categoryId,metaDataFieldValueDtos);
    }

    @PutMapping(path = "/update")
    public String updateValue(@RequestBody MetaDataFieldValueDto metaDataFieldValueDto){
        return categoryService.updateValue(metaDataFieldValueDto);
    }
}

