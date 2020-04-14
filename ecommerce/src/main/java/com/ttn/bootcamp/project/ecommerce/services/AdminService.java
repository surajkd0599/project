package com.ttn.bootcamp.project.ecommerce.services;

import com.ttn.bootcamp.project.ecommerce.exceptions.UserNotFoundException;
import com.ttn.bootcamp.project.ecommerce.models.Customer;
import com.ttn.bootcamp.project.ecommerce.models.Seller;
import com.ttn.bootcamp.project.ecommerce.models.User;
import com.ttn.bootcamp.project.ecommerce.repos.CustomerRepository;
import com.ttn.bootcamp.project.ecommerce.repos.SellerRepository;
import com.ttn.bootcamp.project.ecommerce.repos.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private SendEmail sendEmail;

    public MappingJacksonValue registeredCustomers(String page,String size, String SortBy){
        List<Customer> customers = customerRepository.findAll(PageRequest.of(Integer.parseInt(page),Integer.parseInt(size), Sort.by(SortBy))).getContent();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("userId","firstName","lastName","email","active");
        FilterProvider filterProvider =  new SimpleFilterProvider().addFilter("CustomerFilter",filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(customers);

        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    public MappingJacksonValue registeredSellers(String page,String size, String SortBy){
        List<Seller> sellers = sellerRepository.findAll(PageRequest.of(Integer.parseInt(page),Integer.parseInt(size), Sort.by(SortBy))).getContent();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("userId","firstName","lastName","email","active","companyName","companyContact","addresses");
        FilterProvider filterProvider =  new SimpleFilterProvider().addFilter("Seller-Filter",filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(sellers);

        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    @Transactional
    public String activateUser(Long userId){
        Optional<User> user = userRepository.findById(userId);
        StringBuilder sb = new StringBuilder();

        if(user.isPresent()){
            boolean flag = user.get().isActive();
            if(!flag){
                user.get().setActive(true);
                userRepository.save(user.get());
                sendEmail.sendEmail("Account Activation","Your account is successfully activated",
                        user.get().getEmail());
                sb.append("Account activated");
            }else {
                sb.append("User is already activated");
            }
        }else {
            throw new UserNotFoundException("User not found");
        }
        return sb.toString();
    }

    @Transactional
    public String deactivateUser(Long userId){
        Optional<User> user = userRepository.findById(userId);
        StringBuilder sb = new StringBuilder();

        if(user.isPresent()){
            boolean flag = user.get().isActive();
            if(flag){
                user.get().setActive(false);
                userRepository.save(user.get());
                sendEmail.sendEmail("Account De-Activation","Your account is successfully de-activated",
                        user.get().getEmail());
                sb.append("Account de-activated");
            }else {
                sb.append("User is already de-activated");
            }
        }else {
            throw new UserNotFoundException("User not found");
        }
        return sb.toString();
    }
}
