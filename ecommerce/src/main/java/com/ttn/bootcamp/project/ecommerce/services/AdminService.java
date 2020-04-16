package com.ttn.bootcamp.project.ecommerce.services;

import com.ttn.bootcamp.project.ecommerce.exceptions.BadRequestException;
import com.ttn.bootcamp.project.ecommerce.exceptions.NotFoundException;
import com.ttn.bootcamp.project.ecommerce.models.Customer;
import com.ttn.bootcamp.project.ecommerce.models.Seller;
import com.ttn.bootcamp.project.ecommerce.models.User;
import com.ttn.bootcamp.project.ecommerce.repos.CustomerRepo;
import com.ttn.bootcamp.project.ecommerce.repos.SellerRepo;
import com.ttn.bootcamp.project.ecommerce.repos.UserRepo;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private SellerRepo sellerRepo;

    @Autowired
    private SendEmail sendEmail;

    public MappingJacksonValue registeredCustomers( String page, String size, String SortBy) {

            List<Customer> customers = customerRepo.findAll(PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by(SortBy))).getContent();
            SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "firstName", "lastName", "email", "active");
            FilterProvider filterProvider = new SimpleFilterProvider().addFilter("CustomerFilter", filter);

            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(customers);

            mappingJacksonValue.setFilters(filterProvider);
            return mappingJacksonValue;
    }

    public MappingJacksonValue registeredSellers(String page, String size, String SortBy) {

            List<Seller> sellers = sellerRepo.findAll(PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by(SortBy))).getContent();

            SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "firstName", "lastName", "email", "active", "companyName", "companyContact", "addresses");
            FilterProvider filterProvider = new SimpleFilterProvider().addFilter("Seller-Filter", filter);

            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(sellers);

            mappingJacksonValue.setFilters(filterProvider);
            return mappingJacksonValue;
    }

    @Transactional
    public String activateUser(Long userId) {
        Optional<User> user = userRepo.findById(userId);
        StringBuilder sb = new StringBuilder();

        if (user.isPresent()) {
            boolean flag = user.get().isActive();
            if (!flag) {
                user.get().setActive(true);
                userRepo.save(user.get());
                sendEmail.sendEmail("Account Activation", "Your account is successfully activated",
                        user.get().getEmail());
                sb.append("Account activated");
            } else {
                throw new BadRequestException("User is already activated");
            }
        } else {
            throw new NotFoundException("User not found");
        }
        return sb.toString();
    }

    @Transactional
    public String deactivateUser(Long userId) {
        Optional<User> user = userRepo.findById(userId);
        StringBuilder sb = new StringBuilder();

        if (user.isPresent()) {
            boolean flag = user.get().isActive();
            if (flag) {
                user.get().setActive(false);
                userRepo.save(user.get());
                sendEmail.sendEmail("Account De-Activation", "Your account is successfully de-activated",
                        user.get().getEmail());
                sb.append("Account de-activated");
            } else {
                throw new BadRequestException("User is already deActivated");
            }
        } else {
            throw new NotFoundException("User not found");
        }
        return sb.toString();
    }
}
