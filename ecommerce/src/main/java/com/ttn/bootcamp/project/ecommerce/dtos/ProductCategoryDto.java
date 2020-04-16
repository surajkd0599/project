package com.ttn.bootcamp.project.ecommerce.dtos;

import javax.validation.constraints.NotEmpty;

public class ProductCategoryDto {

    private Long id;

    @NotEmpty
    private String categoryName;

    private Long parentId;

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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
