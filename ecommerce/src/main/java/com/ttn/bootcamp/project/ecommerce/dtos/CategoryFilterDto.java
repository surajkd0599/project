package com.ttn.bootcamp.project.ecommerce.dtos;

import java.util.List;

public class CategoryFilterDto {

    private CategoryFieldValueDto categoryFieldValueDto;

    private List<String> brands;

    private Double minimumPrice;

    private Double maximumPrice;

    public CategoryFieldValueDto getCategoryFieldValueDto() {
        return categoryFieldValueDto;
    }

    public void setCategoryFieldValueDto(CategoryFieldValueDto categoryFieldValueDto) {
        this.categoryFieldValueDto = categoryFieldValueDto;
    }

    public List<String> getBrands() {
        return brands;
    }

    public void setBrands(List<String> brands) {
        this.brands = brands;
    }

    public Double getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(Double minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public Double getMaximumPrice() {
        return maximumPrice;
    }

    public void setMaximumPrice(Double maximumPrice) {
        this.maximumPrice = maximumPrice;
    }
}
