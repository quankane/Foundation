package com.group2.restaurantorderingwebapp.service;

import com.group2.restaurantorderingwebapp.dto.request.ChangePasswordRequest;
import com.group2.restaurantorderingwebapp.dto.request.LoginRequest;
import com.group2.restaurantorderingwebapp.dto.request.RegisterRequest;
import com.group2.restaurantorderingwebapp.dto.response.JwtAuthResponse;
import com.group2.restaurantorderingwebapp.dto.response.UserResponse;
import jakarta.mail.MessagingException;

public interface AuthenticationService {
    JwtAuthResponse login (LoginRequest loginRequest);

    JwtAuthResponse refreshToken(String refreshToken);

    String changePassword(ChangePasswordRequest changePasswordRequest, Long userId);

    UserResponse register(RegisterRequest registerRequest) throws MessagingException;


    String resendOTP(String emailOrPhone) throws MessagingException;

    String verifyEmail(Integer OTP, String emailOrPhone);
}
