package com.ttn.bootcamp.project.ecommerce.services;

import com.ttn.bootcamp.project.ecommerce.dtos.CustomerDto;
import com.ttn.bootcamp.project.ecommerce.dtos.SellerDto;
import com.ttn.bootcamp.project.ecommerce.models.Seller;
import com.ttn.bootcamp.project.ecommerce.models.User;
import com.ttn.bootcamp.project.ecommerce.repos.SellerRepository;
import com.ttn.bootcamp.project.ecommerce.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DtoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

    public String validateCustomer(CustomerDto customerDto){
        StringBuilder sb = new StringBuilder();
        User user = userRepository.findByEmail(customerDto.getEmail());
        if (null!=user){
           sb.append("Email already exist");
        }else if(!customerDto.getPassword().equals(customerDto.getConfirmPassword())){
            sb.append("Passswords not matched");
        }else {
            sb.append("validated");
        }
        return sb.toString();
    }

    public String validateSeller(SellerDto sellerDto){
        StringBuilder sb = new StringBuilder();
        System.out.println("Seller Dto is : "+sellerDto);
        User user = userRepository.findByEmail(sellerDto.getEmail());

        System.out.println("Seller is : "+user);

        System.out.println("Gst number is : "+sellerDto.getGst());
        Seller seller = sellerRepository.findByGst(sellerDto.getGst());
        if (null!=user){
            sb.append("Email already exist");
        }else if(!sellerDto.getPassword().equals(sellerDto.getConfirmPassword())){
            sb.append("Passwords not matched");
        }else if(null!=seller){
            System.out.println("Seller Gst : "+seller);
            sb.append("Gst number already exist");
        }else {
            sb.append("validated");
        }
        return sb.toString();
    }
}
