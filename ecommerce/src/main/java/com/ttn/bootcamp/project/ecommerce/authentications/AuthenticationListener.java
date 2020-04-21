package com.ttn.bootcamp.project.ecommerce.authentications;

import com.ttn.bootcamp.project.ecommerce.exceptions.NotFoundException;
import com.ttn.bootcamp.project.ecommerce.services.UserAttemptService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.LinkedHashMap;

@Component
public class AuthenticationListener implements ApplicationListener<AbstractAuthenticationEvent> {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationListener.class);
    @Autowired
    private UserAttemptService userAttemptService;

    @Override
    @Transactional
    public void onApplicationEvent(AbstractAuthenticationEvent appEvent) {
        LOGGER.debug("In auth listener appevent");
        if (appEvent instanceof AuthenticationSuccessEvent) {
            AuthenticationSuccessEvent event = (AuthenticationSuccessEvent) appEvent;

            String username = "";
            LinkedHashMap<String, String> userMap = new LinkedHashMap<>();

            try {
                userMap = (LinkedHashMap<String, String>) event.getAuthentication().getDetails();
            } catch (ClassCastException ex) {
                LOGGER.error("ClassCastException --{}", ex);
            }

            try {
                username = userMap.get("username");
            } catch (NullPointerException e) {
                LOGGER.error("NullPointerException --{}", e);
            }

            userAttemptService.userSuccessAttempt(username);
        }

        if (appEvent instanceof AuthenticationFailureBadCredentialsEvent) {
            AuthenticationFailureBadCredentialsEvent event = (AuthenticationFailureBadCredentialsEvent) appEvent;

            LinkedHashMap<String, String> usermap = new LinkedHashMap<>();
            usermap = (LinkedHashMap<String, String>) event.getAuthentication().getDetails();

            String username = usermap.get("username");

            if (username != null) {
                userAttemptService.userLoginAttempt(username);
            } else {
                throw new NotFoundException("Email not found");
            }
        }
    }
}
