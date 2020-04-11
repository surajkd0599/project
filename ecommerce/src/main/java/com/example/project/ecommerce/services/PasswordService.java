package com.example.project.ecommerce.services;

import com.example.project.ecommerce.model.User;
import com.example.project.ecommerce.repos.PasswordRepo;
import com.example.project.ecommerce.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

@Service
public class PasswordService {

    @Autowired
    private PasswordRepo passwordRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SendEmail sendEmail;

    @Autowired
    PasswordEncoder passwordEncoder ;

    @Transactional
    @Modifying
    public String updatePassword(String email, String oldPass, String newPass, String confirmPass){

        StringBuilder sb = new StringBuilder();
        User user = userRepository.findByEmail(email);

                    if(passwordEncoder.matches(oldPass,user.getPassword())){

                        if(newPass.equals(confirmPass)){
                                user.setPassword(passwordEncoder.encode(newPass));
                                userRepository.save(user);
                                passwordRepo.deleteByUserEmail(email);

                                sendEmail.sendEmail("Password Changed","Your password has changed",email);

                                sb.append("Password successfully changed");
                        }else{
                            sb.append("New password and confirm password not matched");
                        }
                    }else {
                        sb.append("Old password is not correct");
                    }

        return sb.toString();
    }
}
