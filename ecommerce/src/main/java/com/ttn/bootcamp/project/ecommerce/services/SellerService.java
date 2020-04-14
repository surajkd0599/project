package com.ttn.bootcamp.project.ecommerce.services;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ttn.bootcamp.project.ecommerce.dtos.SellerDto;
import com.ttn.bootcamp.project.ecommerce.models.Seller;
import com.ttn.bootcamp.project.ecommerce.repos.SellerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    public MappingJacksonValue getSellerProfile(Long userId){
        Seller seller = sellerRepository.findByUserId(userId);

        if(seller != null) {
            SellerDto sellerDto = new SellerDto();
            BeanUtils.copyProperties(seller,sellerDto);

            SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept("password","confirmPassword","accountNonLocked","roles");
            FilterProvider filterProvider = new SimpleFilterProvider().addFilter("SellerDto-Filter",filter);

            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(seller);
            mappingJacksonValue.setFilters(filterProvider);

            return mappingJacksonValue;
        }else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
