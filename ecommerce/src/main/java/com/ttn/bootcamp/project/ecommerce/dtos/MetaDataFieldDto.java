package com.ttn.bootcamp.project.ecommerce.dtos;

import com.ttn.bootcamp.project.ecommerce.models.CategoryMetaDataFieldValues;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

public class MetaDataFieldDto {

    private Long id;

    @NotEmpty
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
