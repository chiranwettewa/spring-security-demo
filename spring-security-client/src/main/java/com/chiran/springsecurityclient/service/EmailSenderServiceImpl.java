package com.chiran.springsecurityclient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService{

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String body, String subject){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("chiranw.15@itfac.mrt.ac.lk");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        System.out.println("Mail sent");
    }
}
