package com.ttn.bootcamp.project.ecommerce.services;

import com.ttn.bootcamp.project.ecommerce.exceptions.BadRequestException;
import com.ttn.bootcamp.project.ecommerce.exceptions.UserNotFoundException;
import com.ttn.bootcamp.project.ecommerce.models.User;
import com.ttn.bootcamp.project.ecommerce.repos.PasswordRepo;
import com.ttn.bootcamp.project.ecommerce.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class PasswordService {

    @Autowired
    private PasswordRepo passwordRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SendEmail sendEmail;

    @Autowired
    PasswordEncoder passwordEncoder ;

    @Transactional
    @Modifying
    public String updatePassword(Long userId, String oldPass, String newPass, String confirmPass){

        StringBuilder sb = new StringBuilder();
        Optional<User> user = userRepo.findById(userId);

        if (user.isPresent()) {
            if (passwordEncoder.matches(oldPass, user.get().getPassword())) {

                if (newPass.equals(confirmPass)) {
                    user.get().setPassword(passwordEncoder.encode(newPass));
                    userRepo.save(user.get());

                    String email = user.get().getEmail();
                    sendEmail.sendEmail("Password Changed", "Your password has changed", email);

                    sb.append("Password successfully changed");
                } else {
                    throw new BadRequestException("New password and confirm password not matched");
                }
            } else {
                throw new BadRequestException("Old password is not correct");
            }
        }else {
            throw new UserNotFoundException("User not found");
        }

        return sb.toString();
    }
}
