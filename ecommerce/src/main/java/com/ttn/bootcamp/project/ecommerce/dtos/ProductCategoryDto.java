package com.ttn.bootcamp.project.ecommerce.dtos;

import com.ttn.bootcamp.project.ecommerce.models.SubCategory;

import java.util.Set;

public class ProductCategoryDto {

    private Long id;

    private String categoryName;

    private Set<SubCategory> subCategories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(Set<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }
}
