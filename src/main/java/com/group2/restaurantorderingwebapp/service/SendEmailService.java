package com.group2.restaurantorderingwebapp.service;

import jakarta.mail.MessagingException;

public interface SendEmailService {


    void sendEmail(String email, Integer otp) throws MessagingException;
}
