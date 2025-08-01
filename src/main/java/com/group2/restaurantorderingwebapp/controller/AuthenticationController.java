package com.group2.restaurantorderingwebapp.controller;

import com.group2.restaurantorderingwebapp.dto.request.ChangePasswordRequest;
import com.group2.restaurantorderingwebapp.dto.request.LoginRequest;
import com.group2.restaurantorderingwebapp.dto.request.RegisterRequest;
import com.group2.restaurantorderingwebapp.dto.request.VerifyRequest;
import com.group2.restaurantorderingwebapp.dto.response.ApiResponse;
import com.group2.restaurantorderingwebapp.dto.response.JwtAuthResponse;
import com.group2.restaurantorderingwebapp.dto.response.UserResponse;
import com.group2.restaurantorderingwebapp.entity.User;
import com.group2.restaurantorderingwebapp.service.AuthenticationService;
import com.group2.restaurantorderingwebapp.service.JwtService;
import com.group2.restaurantorderingwebapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @Operation(summary = "Login", description = "Login API")
    @PostMapping(value = {"/login","/signin"})
    public ResponseEntity<JwtAuthResponse>login (@RequestBody LoginRequest loginRequest){
        JwtAuthResponse authenticationUser =  authenticationService.login(loginRequest);

        return ResponseEntity.ok(authenticationUser);
    }

    @PostMapping("/refresh/{refreshToken}")
    @Operation(summary = "REFRESH TOKEN")
    public ResponseEntity<JwtAuthResponse> refreshToken(@PathVariable String refreshToken){
        JwtAuthResponse authResponse = authenticationService.refreshToken(refreshToken);
        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Register", description = "Register API")
    @PostMapping(value = {"/register","/signup"})
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest registerRequest) throws MessagingException {
        ApiResponse apiResponse = ApiResponse.success(authenticationService.register(registerRequest));
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PostMapping("/change-password/{userId}")
    @Operation(summary = "Change Password", description = "Change Password API")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, @PathVariable Long userId){
        String response = authenticationService.changePassword(changePasswordRequest, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verify-account")
    @Operation(summary = "Verify Account", description = "Verify Account API")
    public ResponseEntity<String> verifyAccount(@RequestBody VerifyRequest verifyRequest){
        String response = authenticationService.verifyEmail(verifyRequest.getOTP(), verifyRequest.getEmailOrPhone());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/resend-otp/{emailOrPhone}")
    @Operation(summary = "Resend OTP", description = "Resend OTP API")
    public ResponseEntity<String> resendOTP(@PathVariable("emailOrPhone") String emailOrPhone) throws MessagingException {
        String response = authenticationService.resendOTP(emailOrPhone);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
