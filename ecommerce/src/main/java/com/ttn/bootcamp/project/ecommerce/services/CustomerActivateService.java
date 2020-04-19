
package com.ttn.bootcamp.project.ecommerce.services;

import com.ttn.bootcamp.project.ecommerce.exceptions.BadRequestException;
import com.ttn.bootcamp.project.ecommerce.exceptions.NotFoundException;
import com.ttn.bootcamp.project.ecommerce.models.VerificationToken;
import com.ttn.bootcamp.project.ecommerce.models.User;
import com.ttn.bootcamp.project.ecommerce.repos.UserRepo;
import com.ttn.bootcamp.project.ecommerce.repos.CustomerActivateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.constraints.Email;
import java.util.Date;
import java.util.UUID;

@Service
public class CustomerActivateService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SendEmail sendEmail;

    @Autowired
    private CustomerActivateRepo customerActivateRepo;

    private HttpServletResponse response;

    @Transactional
    public String activateCustomer(String token) {

        VerificationToken customerActivate = customerActivateRepo.findByToken(token);
        StringBuilder sb = new StringBuilder();
        User user = null;

        if (null != customerActivate) {
            try {
                String email = customerActivate.getUserEmail();
                if (!email.equals(null)) {
                    boolean flag = isTokenExpired(email, customerActivate);
                    if (!flag) {
                        user = userRepo.findByEmail(customerActivate.getUserEmail());
                        boolean isActivated = activateCustomer(email, user);
                        if (isActivated) {
                            sb.append("Successfully activated");
                        }
                    } else {
                        throw new BadRequestException("Token Expired");
                    }
                }
            } catch (NullPointerException ex) {
                throw new NotFoundException("No email found");
            }

        } else {
            throw new NotFoundException("Invalid Token");
        }
        return sb.toString();
    }

    boolean activateCustomer(String email, User user) {
        boolean flag = false;
        try {
            user.setActive(true);
            userRepo.save(user);
            sendEmail.sendEmail("ACCOUNT ACTIVATED", "Your account has been activated", email);
            customerActivateRepo.deleteByUserEmail(email);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    boolean isTokenExpired(String email, VerificationToken customerActivate) {

        Date date = new Date();
        long diff = date.getTime() - customerActivate.getGeneratedDate().getTime();
        long diffHours = diff / (60 * 60 * 1000);
        boolean flag = false;
        // token expire case
        if (diffHours > 24) {
            customerActivateRepo.deleteByUserEmail(email);

            String newToken = UUID.randomUUID().toString();

            VerificationToken localCustomerActivate = new VerificationToken();
            localCustomerActivate.setToken(newToken);
            localCustomerActivate.setUserEmail(email);
            localCustomerActivate.setGeneratedDate(new Date());

            customerActivateRepo.save(localCustomerActivate);

            sendEmail.sendEmail("RE-ACCOUNT ACTIVATE TOKEN", "To confirm your account, please click here : http://localhost:8080/ecommerce/register/confirm-account?token=" + newToken, email);
            flag = true;
        }
        return flag;
    }

    @Transactional
    public String resendLink(String email) {

        User user = userRepo.findByEmail(email);
        StringBuilder sb = new StringBuilder();
        try {
            if (!user.getEmail().equals(null)) {
                if (user.isActive()) {
                    throw new BadRequestException("Account already active");
                } else {
                    customerActivateRepo.deleteByUserEmail(email);

                    String newToken = UUID.randomUUID().toString();

                    VerificationToken localCustomerActivate = new VerificationToken();
                    localCustomerActivate.setToken(newToken);
                    localCustomerActivate.setUserEmail(email);
                    localCustomerActivate.setGeneratedDate(new Date());

                    customerActivateRepo.save(localCustomerActivate);

                    sendEmail.sendEmail("RE-ACCOUNT ACTIVATE TOKEN", "To confirm your account, please click here : http://localhost:8080/ecommerce/register/confirm-account?token=" + newToken, email);

                    sb.append("Successful");
                }
            }
        } catch (NullPointerException ex) {
            throw new NotFoundException("No email found");
        }
        return sb.toString();
    }
}

