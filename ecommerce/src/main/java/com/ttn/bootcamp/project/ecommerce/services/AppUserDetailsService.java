package com.ttn.bootcamp.project.ecommerce.services;

import com.ttn.bootcamp.project.ecommerce.daos.UserDao;
import com.ttn.bootcamp.project.ecommerce.dtos.CustomerDto;
import com.ttn.bootcamp.project.ecommerce.dtos.SellerDto;
import com.ttn.bootcamp.project.ecommerce.exceptions.BadRequestException;
import com.ttn.bootcamp.project.ecommerce.models.*;
import com.ttn.bootcamp.project.ecommerce.repos.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class AppUserDetailsService implements UserDetailsService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDao userDao;


    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private SellerRepo sellerRepo;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private CustomerActivateRepo customerActivateRepo;

    @Autowired
    private SendEmail sendEmail;

    @Transactional
    public String registerCustomer(CustomerDto customerDto) {

        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDto, customer);
        String pass = passwordEncoder.encode(customer.getPassword());

        Set<Role> roles = new HashSet<>();
        roles.add(new Role("CUSTOMER"));

        customer.setPassword(pass);
        customer.setRoles(roles);

        customerRepo.save(customer);

        String token = UUID.randomUUID().toString();

        VerificationToken customerActivate = new VerificationToken();
        customerActivate.setToken(token);
        customerActivate.setUserEmail(customer.getEmail());
        customerActivate.setGeneratedDate(new Date());

        customerActivateRepo.save(customerActivate);
        String email = customer.getEmail();

        sendEmail.sendEmail("ACCOUNT ACTIVATE TOKEN", "To confirm your account, please click here : "
                + "http://localhost:8080/ecommerce/register/confirm-account?token=" + token, email);

        return "Registration Successful";
    }

    @Transactional
    public String registerSeller(SellerDto sellerDto) {
        Seller seller = new Seller();
        BeanUtils.copyProperties(sellerDto, seller);

        if (seller.getAddresses().size() == 1) {
            String pass = passwordEncoder.encode(seller.getPassword());
            seller.setPassword(pass);
            sellerRepo.save(seller);
            return "Registration Successful";
        } else {
            throw new BadRequestException("Seller cannot have multiple addresses");
        }
    }

    @Transactional
    public User registerAdmin(Admin admin) {
        String pass = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(pass);
        return adminRepo.save(admin);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userDao.loadUserByUsername(username);
        System.out.println("User Details : " + userDetails);
        return userDetails;
    }

    /*@Transactional
    @Modifying
    public void updateUserUsername(String username,String userName){
        Long userId = userRepository.findUserId(username);
        userRepository.updateUserUsername(userId,userName);
    }

    @Transactional
    @Modifying
    public void updateUserFirstName(String username,String firstName){
        Long userId = userRepository.findUserId(username);
        userRepository.updateUserFirstName(userId,firstName);
    }

    @Transactional
    @Modifying
    public void updateUserLastName(String username,String lastName){
        Long userId = userRepository.findUserId(username);
        userRepository.updateUserLastName(userId,lastName);
    }

    @Transactional
    @Modifying
    public void updateUserEmail(String username,String email){
        Long userId = userRepository.findUserId(username);
        userRepository.updateUserEmail(userId,email);
    }

    @Transactional
    @Modifying
    public void updateUserPassword(String username,String password){
        Long userId = userRepository.findUserId(username);
        userRepository.updateUserPassword(userId,password);
    }*/
}
