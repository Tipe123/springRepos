package com.spring.security.event.listener;

import com.spring.security.event.RegistrationCompleteEvent;
import com.spring.security.model.User;
import com.spring.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //Create the Verification token For the user

        User user =  event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token,user);

        // Send mail to user

        String url = event.getApplicationUrl() + "/verifyRegistration?token="+token;


        //SendVerificationEmail()
        log.info("Click the link to verify your account: {} ",url);
    }
}
