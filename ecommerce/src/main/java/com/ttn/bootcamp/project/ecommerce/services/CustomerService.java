package com.ttn.bootcamp.project.ecommerce.services;

import com.ttn.bootcamp.project.ecommerce.dtos.AddressDto;
import com.ttn.bootcamp.project.ecommerce.dtos.CustomerDto;
import com.ttn.bootcamp.project.ecommerce.dtos.UserProfileDto;
import com.ttn.bootcamp.project.ecommerce.exceptions.NotFoundException;
import com.ttn.bootcamp.project.ecommerce.models.Address;
import com.ttn.bootcamp.project.ecommerce.models.Customer;
import com.ttn.bootcamp.project.ecommerce.repos.AddressRepo;
import com.ttn.bootcamp.project.ecommerce.repos.CustomerRepo;
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
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AddressRepo addressRepo;

    public UserProfileDto getCustomerProfile(Long id){
        Optional<Customer> customer = customerRepo.findById(id);

        if(customer.isPresent()) {
            UserProfileDto userProfileDto = new UserProfileDto();
            BeanUtils.copyProperties(customer.get(), userProfileDto);

            return userProfileDto;
        }else {
            throw new NotFoundException("User not found");
        }
    }

    public MappingJacksonValue getCustomerAddress(Long userId){
        Optional<Customer> customer = customerRepo.findById(userId);

        if (customer.isPresent()) {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer.get(), customerDto);

            SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("addresses");
            FilterProvider filterProvider = new SimpleFilterProvider().addFilter("CustomerDto-Filter", filter);

            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(customerDto);
            mappingJacksonValue.setFilters(filterProvider);

            return mappingJacksonValue;
        }else {
            throw new NotFoundException("User not found");
        }
    }

    @Transactional
    @Modifying
    public String updateCustomer(UserProfileDto userProfileDto, Long userId){
        Optional<Customer> customerExist = customerRepo.findById(userId);

        StringBuilder sb = new StringBuilder();
        if(customerExist.isPresent()) {
            customerExist.get().setFirstName(userProfileDto.getFirstName());
            customerExist.get().setMobileNo(userProfileDto.getMobileNo());
            customerExist.get().setUsername(userProfileDto.getUsername());
            customerExist.get().setMobileNo(userProfileDto.getMobileNo());

            customerRepo.save(customerExist.get());

            sb.append("User updated");
        }else {
            throw new NotFoundException("User not found");
        }
        return sb.toString();
    }

    @Transactional
    @Modifying
    public String addAddress(AddressDto addressDto, Long userId){
        Optional<Customer> customer = customerRepo.findById(userId);

        StringBuilder sb = new StringBuilder();
        if(customer.isPresent()) {
            addressDto.setUserId(userId);
            Address address = new Address();
            BeanUtils.copyProperties(addressDto,address);

            address.setDeleted(true);
            addressRepo.save(address);

            sb.append("Address added");

        }else {
            throw new NotFoundException("User not found");
        }
        return sb.toString();
    }

    @Transactional
    @Modifying
    public String deleteAddress(Long addressId){
        Optional<Address> address = addressRepo.findById(addressId);

        StringBuilder sb = new StringBuilder();
        if (address.isPresent()){
            address.get().setDeleted(false);
            addressRepo.save(address.get());
            //addressRepo.deleteByAddressId(addressId);
            sb.append("Address deleted");
        }else {
            throw new NotFoundException("Address not found");
        }
        return sb.toString();
    }

    @Transactional
    @Modifying
    public String updateAddress(AddressDto addressDto, Long addressId,Long userId){

        Optional<Customer> customer = customerRepo.findById(userId);

        if(customer.isPresent()) {
            Optional<Address> addressExist = addressRepo.findById(addressId);
            StringBuilder sb = new StringBuilder();

            if (addressExist.isPresent()) {
                Address address = new Address();
                BeanUtils.copyProperties(addressDto, address);

                address.setDeleted(true);

                addressRepo.save(address);

                sb.append("Address updated");
            } else {
                throw new NotFoundException("Address not found");
            }
            return sb.toString();
        }else {
            throw new NotFoundException("User not found");
        }

    }
}
