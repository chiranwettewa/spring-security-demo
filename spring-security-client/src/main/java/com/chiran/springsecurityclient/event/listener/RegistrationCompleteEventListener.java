package com.chiran.springsecurityclient.event.listener;

import com.chiran.springsecurityclient.entity.User;
import com.chiran.springsecurityclient.event.RegistrationCompleteEvent;
import com.chiran.springsecurityclient.service.EmailSenderServiceImpl;
import com.chiran.springsecurityclient.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent>{


    @Autowired
    private EmailSenderServiceImpl emailSenderService;
    @Autowired
    private UserService userService;


    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(user, token);

        String url = event.getApplicationUrl() + "/verifyRegistration?token=" + token;

        emailSenderService.sendEmail(user.getEmail(),"Click below link to verify the password. "  + "\r\n" +url,"Verification token for spring security demo application");

        log.info("Click to verify : {}",url);
    }

}
