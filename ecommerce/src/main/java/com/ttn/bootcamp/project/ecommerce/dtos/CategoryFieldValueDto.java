package com.ttn.bootcamp.project.ecommerce.dtos;

import java.util.Map;
import java.util.Objects;

public class CategoryFieldValueDto {

    private Long parentId;

    private String categoryName;

    private Long categoryId;

    private Map<String, String> fieldValueMap;

    public Map<String, String> getFieldValueMap() {
        return fieldValueMap;
    }

    public void setFieldValueMap(Map<String, String> fieldValueMap) {
        this.fieldValueMap = fieldValueMap;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryFieldValueDto that = (CategoryFieldValueDto) o;
        return Objects.equals(parentId, that.parentId) &&
                Objects.equals(categoryName, that.categoryName) &&
                Objects.equals(categoryId, that.categoryId) &&
                Objects.equals(fieldValueMap, that.fieldValueMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentId, categoryName, categoryId, fieldValueMap);
    }

    @Override
    public String toString() {
        return "CategoryFieldValueDto{" +
                "parentId=" + parentId +
                ", categoryName='" + categoryName + '\'' +
                ", categoryId=" + categoryId +
                ", fieldValueMap=" + fieldValueMap +
                '}';
    }
}
