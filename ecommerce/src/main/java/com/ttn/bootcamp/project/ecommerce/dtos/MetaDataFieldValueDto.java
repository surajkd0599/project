package com.ttn.bootcamp.project.ecommerce.dtos;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class MetaDataFieldValueDto implements Serializable {

    private Long categoryId;

    private Long fieldId;

    @NotEmpty
    private String value;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
