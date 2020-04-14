package com.ttn.bootcamp.project.ecommerce.services;

import com.ttn.bootcamp.project.ecommerce.dtos.AddressDto;
import com.ttn.bootcamp.project.ecommerce.dtos.CustomerDto;
import com.ttn.bootcamp.project.ecommerce.dtos.UserProfile;
import com.ttn.bootcamp.project.ecommerce.exceptions.UserNotFoundException;
import com.ttn.bootcamp.project.ecommerce.models.Address;
import com.ttn.bootcamp.project.ecommerce.models.Customer;
import com.ttn.bootcamp.project.ecommerce.repos.AddressRepo;
import com.ttn.bootcamp.project.ecommerce.repos.CartRepository;
import com.ttn.bootcamp.project.ecommerce.repos.CustomerRepository;
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

    @Autowired
    private AddressRepo addressRepo;

    public UserProfile getCustomerProfile(Long userId){
        Customer customer = customerRepository.findByUserId(userId);

        if(customer != null) {
            UserProfile userProfile = new UserProfile();
            BeanUtils.copyProperties(customer, userProfile);

            return userProfile;
        }else {
            throw new UserNotFoundException("User not found");
        }
    }

    public MappingJacksonValue getCustomerAddress(Long userId){
        Customer customer = customerRepository.findByUserId(userId);

        if (customer != null) {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);

            SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("addresses");
            FilterProvider filterProvider = new SimpleFilterProvider().addFilter("CustomerDto-Filter", filter);

            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(customerDto);
            mappingJacksonValue.setFilters(filterProvider);

            return mappingJacksonValue;
        }else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Transactional
    @Modifying
    public String updateCustomer(UserProfile userProfile, Long userId){
        Customer customerExist = customerRepository.findByUserId(userId);

        StringBuilder sb = new StringBuilder();
        if(customerExist != null) {
            customerExist.setFirstName(userProfile.getFirstName());
            customerExist.setMobileNo(userProfile.getMobileNo());
            customerExist.setUsername(userProfile.getUsername());
            customerExist.setEmail(userProfile.getEmail());
            customerExist.setMobileNo(userProfile.getMobileNo());

            customerRepository.save(customerExist);

            sb.append("User updated");
        }else {
            throw new UserNotFoundException("User not found");
        }
        return sb.toString();
    }

    @Transactional
    @Modifying
    public String addAddress(AddressDto addressDto, Long userId){
        Customer customer = customerRepository.findByUserId(userId);

        StringBuilder sb = new StringBuilder();
        if(customer != null) {
            addressDto.setUserId(userId);
            Address address = new Address();
            BeanUtils.copyProperties(addressDto,address);

            address.setDeleted(true);
            addressRepo.save(address);

            sb.append("Address added");

        }else {
            throw new UserNotFoundException("User not found");
        }
        return sb.toString();
    }

    @Transactional
    @Modifying
    public String deleteAddress(Long addressId){
        Address address = addressRepo.findByAddressId(addressId);

        StringBuilder sb = new StringBuilder();
        if (address != null){
            address.setDeleted(false);
            addressRepo.save(address);
            //addressRepo.deleteByAddressId(addressId);
            sb.append("Address deleted");
        }else {
            throw new UserNotFoundException("Address not found");
        }
        return sb.toString();
    }

    @Transactional
    @Modifying
    public String updateAddress(AddressDto addressDto, Long addressId){
        Address addressExist = addressRepo.findByAddressId(addressId);
        StringBuilder sb = new StringBuilder();

        if (addressExist != null){
            Address address = new Address();

            System.out.println("Status deleted : "+addressDto.isActive());
            BeanUtils.copyProperties(addressDto,address);

            address.setDeleted(true);

            addressRepo.save(address);

            sb.append("Address updated");
        }else {
            throw new UserNotFoundException("Address not found");
        }
        return sb.toString();
    }
}
