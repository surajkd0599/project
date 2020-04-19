package com.ttn.bootcamp.project.ecommerce.services;

import com.ttn.bootcamp.project.ecommerce.dtos.CustomerDto;
import com.ttn.bootcamp.project.ecommerce.dtos.SellerDto;
import com.ttn.bootcamp.project.ecommerce.exceptions.BadRequestException;
import com.ttn.bootcamp.project.ecommerce.models.Seller;
import com.ttn.bootcamp.project.ecommerce.models.User;
import com.ttn.bootcamp.project.ecommerce.repos.SellerRepo;
import com.ttn.bootcamp.project.ecommerce.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DtoService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SellerRepo sellerRepo;

    public void validateCustomer(CustomerDto customerDto) {

        User user = userRepo.findByEmail(customerDto.getEmail());
        if (null != user) {
            throw new BadRequestException("Email already exist");
        } else if (!customerDto.getPassword().equals(customerDto.getConfirmPassword())) {
            throw new BadRequestException("Passswords not matched");
        }
    }

    public void validateSeller(SellerDto sellerDto) {

        User user = userRepo.findByEmail(sellerDto.getEmail());

        Seller seller = sellerRepo.findByGst(sellerDto.getGst());

        Seller seller1 = sellerRepo.findByCompanyName(sellerDto.getCompanyName());

        if (null != user) {
            throw new BadRequestException("Email already exist");
        } else if (!sellerDto.getPassword().equals(sellerDto.getConfirmPassword())) {
            throw new BadRequestException("Passwords not matched");
        } else if (null != seller) {
            throw new BadRequestException("Gst number already exist");
        } else if (null != seller1) {
            throw new BadRequestException("Company name already exist");
        }
    }
}
