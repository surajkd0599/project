
package com.example.project.ecommerce.services;

import com.example.project.ecommerce.exception.UserNotFoundException;
import com.example.project.ecommerce.model.VerificationToken;
import com.example.project.ecommerce.model.User;
import com.example.project.ecommerce.repos.UserRepository;
import com.example.project.ecommerce.repos.CustomerActivateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

@Service
public class CustomerActivateService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SendEmail sendEmail;

    @Autowired
    private CustomerActivateRepo customerActivateRepo;

    private HttpServletResponse response;

    @Transactional
    public String activateCustomer(String token) {


        VerificationToken customerActivate = customerActivateRepo.findByToken(token);
        StringBuilder sb=new StringBuilder();
        User user=null;

        if(null!=customerActivate)
        {
            try {
                String email=customerActivate.getUserEmail();
                if (!email.equals(null)) {
                    boolean flag=isTokenExpired(email, customerActivate);
                    if(!flag) {
                        user = userRepository.findByEmail(customerActivate.getUserEmail());
                        boolean isActivated= activateCustomer(email,user);
                        if(isActivated)
                        {
                            sb.append("Successfully activated");
                        }
                    }else {
                        sb.append("Token Expired");
                    }
                }
            } catch (NullPointerException ex) {
                throw new UserNotFoundException("No email found");
            }

        }else{
            sb.append("Invalid Token");
        }
        return sb.toString();
    }

    boolean activateCustomer(String email, User user){
        boolean flag=false;
        try {
            user.setActive(true);
            userRepository.save(user);
            sendEmail.sendEmail("ACCOUNT ACTIVATED", "Your account has been activated", email);
            customerActivateRepo.deleteByUserEmail(email);
            flag=true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    boolean isTokenExpired(String email, VerificationToken customerActivate ){

        Date date = new Date();
        long diff = date.getTime() - customerActivate.getGeneratedDate().getTime();
        long diffHours = diff / (60 * 60 * 1000);
        boolean flag=false;
        // token expire case
        if (diffHours > 24) {
            customerActivateRepo.deleteByUserEmail(email);

            String newToken = UUID.randomUUID().toString();

            VerificationToken localCustomerActivate = new VerificationToken();
            localCustomerActivate.setToken(newToken);
            localCustomerActivate.setUserEmail(email);
            localCustomerActivate.setGeneratedDate(new Date());

            customerActivateRepo.save(localCustomerActivate);

            sendEmail.sendEmail("RE-ACCOUNT ACTIVATE TOKEN","To confirm your account, please click here : "
                    +"http://localhost:8080/ecommerce/register/confirm-account?token="+newToken,email);
            flag=true;
        }
        return flag;
    }

    @Transactional
    public String resendLink(String email) {

        User user = userRepository.findByEmail(email);
        StringBuilder sb = new StringBuilder();
        try {
            if (!user.getEmail().equals(null)) {
                if (user.isActive()) {
                   return sb.append("Account already active").toString();
                }else {
                    customerActivateRepo.deleteByUserEmail(email);

                    String newToken = UUID.randomUUID().toString();

                    VerificationToken localCustomerActivate = new VerificationToken();
                    localCustomerActivate.setToken(newToken);
                    localCustomerActivate.setUserEmail(email);
                    localCustomerActivate.setGeneratedDate(new Date());

                    customerActivateRepo.save(localCustomerActivate);

                    sendEmail.sendEmail("RE-ACCOUNT ACTIVATE TOKEN","To confirm your account, please click here : "
                            +"http://localhost:8080/ecommerce/register/confirm-account?token="+newToken,email);

                    sb.append("Successful");
                }
            }
        } catch (NullPointerException ex) {
            throw new UserNotFoundException("No email found");
        }
        return sb.toString();
    }
}

