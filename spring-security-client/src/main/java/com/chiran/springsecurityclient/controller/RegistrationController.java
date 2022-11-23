package com.chiran.springsecurityclient.controller;

import com.chiran.springsecurityclient.entity.User;
import com.chiran.springsecurityclient.entity.VerificationToken;
import com.chiran.springsecurityclient.event.RegistrationCompleteEvent;
import com.chiran.springsecurityclient.model.UserModel;
import com.chiran.springsecurityclient.service.EmailSenderServiceImpl;
import com.chiran.springsecurityclient.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSenderServiceImpl emailSenderService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request){
        System.out.println("request -----"+request);
        User user =  userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(user,applicationUrl(request)));

        return "Success";
    }
    @GetMapping("/verifyRegistration")
    public String registerUser(@RequestParam("token") String token){
        String result  =  userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")){
            return "User verify successfully";
        }
        return "Bad user";
    }
    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken, final HttpServletRequest request){
        VerificationToken verificationToken  =  userService.genarateNewVerificationToken(oldToken);

        User user = verificationToken.getUser();
        resendVerificationEmail(user, applicationUrl(request),verificationToken);
        return "Verification link sent.";
    }

    private void resendVerificationEmail(User user, String applicationUrl, VerificationToken verificationToken) {
        String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken();

        emailSenderService.sendEmail(user.getEmail(),"Click below link to verify the password. "  + "\r\n" +url,"Verification token for spring security demo application");

        log.info("Click to verify : {}",url);
    }

    private String applicationUrl(HttpServletRequest request) {

        return "http://" + request.getServerName()
                + ":"
                + request.getServerPort();
    }

}
