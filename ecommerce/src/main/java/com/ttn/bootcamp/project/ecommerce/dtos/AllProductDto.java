package com.ttn.bootcamp.project.ecommerce.dtos;

import com.ttn.bootcamp.project.ecommerce.models.Product;

import java.util.List;
import java.util.Set;

public class AllProductDto {

    private Long categoryId;

    private List<Product> productList;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
