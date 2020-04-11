package com.example.project.ecommerce.services;

import com.example.project.ecommerce.dtos.CustomerDto;
import com.example.project.ecommerce.model.Cart;
import com.example.project.ecommerce.model.Customer;
import com.example.project.ecommerce.repos.CartRepository;
import com.example.project.ecommerce.repos.CustomerRepository;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public MappingJacksonValue getCustomerProfile(String email){
        Customer customer = customerRepository.findByEmail(email);
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer,customerDto);

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept("password","confirmPassword","accountNonLocked","roles");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("CustomerDto-Filter",filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(customerDto);
        mappingJacksonValue.setFilters(filterProvider);

        return mappingJacksonValue;
    }

    @Transactional
    @Modifying
    public void updateCustomer(CustomerDto customerDto){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDto,customer);
        String pass = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(pass);
        customerRepository.save(customer);
    }

    public Cart addToCart(Cart cart){
        return cartRepository.save(cart);
    }
}
