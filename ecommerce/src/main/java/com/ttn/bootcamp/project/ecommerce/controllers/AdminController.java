package com.ttn.bootcamp.project.ecommerce.controllers;

import com.ttn.bootcamp.project.ecommerce.dtos.*;
import com.ttn.bootcamp.project.ecommerce.repos.ProductVariationRepo;
import com.ttn.bootcamp.project.ecommerce.services.AdminService;
import com.ttn.bootcamp.project.ecommerce.services.CategoryService;
import com.ttn.bootcamp.project.ecommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
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
    public MappingJacksonValue getCustomers(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "id") String sortBy) {
        return adminService.registeredCustomers(page, size, sortBy);
    }

    @GetMapping(path = "/sellers")
    public MappingJacksonValue getSellers(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "id") String sortBy) {
        return adminService.registeredSellers(page, size, sortBy);
    }

    @PatchMapping(path = "/activateCustomer/{id}")
    public String activateCustomer(@PathVariable(value = "id") Long id) {
        return adminService.activateUser(id);
    }

    @PatchMapping(path = "/deActivateCustomer/{id}")
    public String deactivateCustomer(@PathVariable(value = "id") Long id) {
        return adminService.deactivateUser(id);
    }

    @PatchMapping(path = "/activateSeller/{id}")
    public String activateSeller(@PathVariable(value = "id") Long id) {
        return adminService.activateUser(id);
    }

    @PatchMapping(path = "/deActivateSeller/{id}")
    public String deactivateSeller(@PathVariable(value = "id") Long id) {
        return adminService.deactivateUser(id);
    }

    //Category Api's

    @PostMapping(path = "/metaDataField")
    public String addMetaDataField(@Valid @RequestBody MetaDataFieldDto metaDataFieldDto, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_CREATED);
        return categoryService.addMetaDataField(metaDataFieldDto);
    }

    @GetMapping(path = "/metaDataField")
    public MappingJacksonValue getMetaDataField(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "ASC") String order) {
        return categoryService.getMetaDataFields(page, size, sortBy, order);
    }

    @PostMapping(path = "/category")
    public String addCategory(@RequestBody ProductCategoryDto productCategoryDto, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_CREATED);
        return categoryService.addCategory(productCategoryDto);
    }

    @GetMapping(path = "/category/{categoryId}")
    public List<ProductCategoryDto> getCategoryById(@PathVariable(value = "categoryId") Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @GetMapping(path = "/category")
    public Set<Set<ProductCategoryDto>> getCategory() {
        return categoryService.getCategory();
    }

    @PutMapping(path = "/category/{categoryId}")
    public String updateCategory(@PathVariable(value = "categoryId") Long categoryId, @RequestBody ProductCategoryDto productCategoryDto) {
        return categoryService.updateCategory(categoryId, productCategoryDto);
    }

    @PostMapping(path = "/category/{categoryId}")
    public String addMetaDataFieldValue(@PathVariable(value = "categoryId") Long categoryId, @RequestBody List<MetaDataFieldValueDto> metaDataFieldValueDtos, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_CREATED);
        return categoryService.addMetaDataFieldValue(categoryId, metaDataFieldValueDtos);
    }

    @PutMapping(path = "/category")
    public String updateMetaDataFieldValue(@RequestBody MetaDataFieldValueDto metaDataFieldValueDto) {
        return categoryService.updateMetaDataFieldValue(metaDataFieldValueDto);
    }

    @GetMapping(path = "/product/{productId}")
    public List<ProductVariationGetDto> getProduct(@PathVariable(value = "productId") Long productId) {
        return productService.getProductForAdmin(productId);
    }

    @GetMapping(path = "/{categoryId}/product")
    public AllProductDto getProducts(@PathVariable(value = "categoryId") Long categoryId) {
        return productService.getAllProductsByCategoryId(categoryId);
    }

    @PutMapping(path = "/product/{productId}/deActivate")
    public String deActivateProduct(@PathVariable(value = "productId") Long productId) {
        return productService.deActivateProduct(productId);
    }

    @PutMapping(path = "/product/{productId}/activate")
    public String activateProduct(@PathVariable(value = "productId") Long productId) {
        return productService.activateProduct(productId);
    }
}

