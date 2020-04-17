package com.ttn.bootcamp.project.ecommerce.dtos;

import org.springframework.stereotype.Component;

@Component
public interface ProductMinMaxPriceDto {

    Double getMinPrice();
    Double getMaxPrice();

}
