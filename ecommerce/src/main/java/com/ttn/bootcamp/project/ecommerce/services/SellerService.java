package com.ttn.bootcamp.project.ecommerce.services;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ttn.bootcamp.project.ecommerce.dtos.AddressDto;
import com.ttn.bootcamp.project.ecommerce.dtos.SellerProfileDto;
import com.ttn.bootcamp.project.ecommerce.exceptions.UserNotFoundException;
import com.ttn.bootcamp.project.ecommerce.models.Address;
import com.ttn.bootcamp.project.ecommerce.models.Seller;
import com.ttn.bootcamp.project.ecommerce.repos.AddressRepo;
import com.ttn.bootcamp.project.ecommerce.repos.SellerRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class SellerService {

    @Autowired
    private SellerRepo sellerRepo;

    @Autowired
    private AddressRepo addressRepo;

    public MappingJacksonValue getSellerProfile(Long userId){
        Optional<Seller> seller = sellerRepo.findById(userId);

        if(seller.isPresent()) {

            SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("userId","username","firstName","lastName","gst","companyContact","companyName","addresses");
            FilterProvider filterProvider = new SimpleFilterProvider().addFilter("Seller-Filter",filter);

            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(seller);
            mappingJacksonValue.setFilters(filterProvider);

            return mappingJacksonValue;
        }else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Transactional
    @Modifying
    public String updateSeller(SellerProfileDto sellerProfileDto, Long userId){
        Optional<Seller> sellerExist = sellerRepo.findById(userId);

        StringBuilder sb = new StringBuilder();
        if(sellerExist.isPresent()) {
            sellerExist.get().setFirstName(sellerProfileDto.getFirstName());
            sellerExist.get().setUsername(sellerProfileDto.getUsername());
            sellerExist.get().setCompanyContact(sellerProfileDto.getCompanyContact());
            sellerExist.get().setGst(sellerProfileDto.getGst());

            sellerRepo.save(sellerExist.get());

            sb.append("User updated");
        }else {
            throw new UserNotFoundException("User not found");
        }
        return sb.toString();
    }

    @Transactional
    @Modifying
    public String updateAddress(AddressDto addressDto, Long addressId,Long userId){

        Optional<Seller> seller = sellerRepo.findById(userId);

        if(seller.isPresent()) {
            Optional<Address> addressExist = addressRepo.findById(addressId);
            StringBuilder sb = new StringBuilder();

            if (addressExist.isPresent()) {
                Address address = new Address();

                System.out.println("Status deleted : " + addressDto.isActive());
                BeanUtils.copyProperties(addressDto, address);

                address.setDeleted(true);

                addressRepo.save(address);

                sb.append("Address updated");
            } else {
                throw new UserNotFoundException("Address not found");
            }
            return sb.toString();
        }else {
            throw new UserNotFoundException("User not found");
        }

    }
}
