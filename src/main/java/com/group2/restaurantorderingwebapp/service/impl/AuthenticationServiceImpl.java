package com.group2.restaurantorderingwebapp.service.impl;

import com.group2.restaurantorderingwebapp.dto.request.ChangePasswordRequest;
import com.group2.restaurantorderingwebapp.dto.request.LoginRequest;
import com.group2.restaurantorderingwebapp.dto.request.RegisterRequest;
import com.group2.restaurantorderingwebapp.dto.response.CartResponse;
import com.group2.restaurantorderingwebapp.dto.response.JwtAuthResponse;
import com.group2.restaurantorderingwebapp.dto.response.UserResponse;
import com.group2.restaurantorderingwebapp.entity.Cart;
import com.group2.restaurantorderingwebapp.entity.Role;
import com.group2.restaurantorderingwebapp.entity.User;
import com.group2.restaurantorderingwebapp.exception.AppException;
import com.group2.restaurantorderingwebapp.exception.ErrorCode;
import com.group2.restaurantorderingwebapp.exception.ResourceNotFoundException;
import com.group2.restaurantorderingwebapp.repository.CartRepository;
import com.group2.restaurantorderingwebapp.repository.RoleRepository;
import com.group2.restaurantorderingwebapp.repository.UserRepository;
import com.group2.restaurantorderingwebapp.service.AuthenticationService;
import com.group2.restaurantorderingwebapp.service.JwtService;
import com.group2.restaurantorderingwebapp.service.SendEmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final SendEmailService sendEmailService;
    private final CartRepository cartRepository;


    @Override
    public JwtAuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByPhoneNumberOrEmail(loginRequest.getEmailOrPhone(), loginRequest.getEmailOrPhone()).orElseThrow(() -> new ResourceNotFoundException("User", "email or phone number", loginRequest.getEmailOrPhone()));
        if (!user.isStatus()) {
            throw new AppException(ErrorCode.USER_NOT_VERIFIED);
        }
        try {

          authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmailOrPhone(), loginRequest.getPassword()
            ));
        } catch (Exception exception) {
            throw new AppException(ErrorCode.USER_UNAUTHENTICATED);
        }

        String jwtToken = jwtService.generateToken(user, jwtService.getExpirationTime()*24*3);
        String refreshToken = jwtService.generateToken(user,jwtService.getExpirationTime()*24*7);
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart", "userId",user.getUsername()));
        userResponse.setCart(cart);
        return JwtAuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(userResponse)
                .expiredTime(new Timestamp(System.currentTimeMillis() +jwtService.getExpirationTime()))
                .build();
    }

    @Override
    public JwtAuthResponse refreshToken(String refreshToken) {
        if (jwtService.isTokenExpired(refreshToken)) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }
         String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByPhoneNumber(username).orElseThrow(() -> new ResourceNotFoundException("User", "email or phone number", username));
        String accessToken = jwtService.generateToken(user, jwtService.getExpirationTime()*24*7) ;
        refreshToken = jwtService.generateToken(user, jwtService.getExpirationTime()*24*2);

        UserResponse userResponse = modelMapper.map(user, UserResponse.class);

        return JwtAuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userResponse)
                .expiredTime(new Timestamp(System.currentTimeMillis() +jwtService.getExpirationTime()))
                .build();
    }

    @Override
    public String changePassword(ChangePasswordRequest changePasswordRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        if (changePasswordRequest.getOldPassword()!= null && !passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
        return "Password changed successfully!.";

    }



    @Override
    public UserResponse register(RegisterRequest registerRequest) throws MessagingException {
        if (userRepository.existsByPhoneNumber(registerRequest.getPhoneNumber())){
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())){
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = modelMapper.map(registerRequest, User.class);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setOtp(generateOTP());

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByRoleName("ROLE_USER").orElseThrow(()->new ResourceNotFoundException("role", "role's name","ROLE_USER"));
        roles.add(role);
        user.setRoles(roles);

        user = userRepository.save(user);
        sendEmailService.sendEmail(user.getEmail(), user.getOtp());
        Cart cart = new Cart();
        cart.setUser(user);
        cart = cartRepository.save(cart);

        UserResponse userResponse =  modelMapper.map(user,UserResponse.class);
        userResponse.setCart(cart);
        return userResponse;
    }

    private Integer generateOTP() {
        Random random = new Random();
        int ans = random.nextInt(899999);
        return 100000 + ans;
    }

    @Override
    public String resendOTP(String emailOrPhone) throws MessagingException {
        User user = userRepository.findByPhoneNumberOrEmailAndStatus(emailOrPhone,emailOrPhone,false).orElseThrow(() -> new ResourceNotFoundException("User", "email or phone number", emailOrPhone));
        Integer OTP = generateOTP();
        user.setOtp(OTP);
        userRepository.save(user);
        sendEmailService.sendEmail(user.getEmail(), OTP);
        return "OTP sent successfully!.";
    }

    @Override
    public String verifyEmail(Integer OTP, String emailOrPhone){
        User user = userRepository.findByPhoneNumberOrEmailAndStatus(emailOrPhone,emailOrPhone,false).orElseThrow(() -> new ResourceNotFoundException("User", "email or phone number", emailOrPhone));
        if (!Objects.equals(user.getOtp(), OTP)){
            throw new AppException(ErrorCode.WRONG_OTP);
        }
        user.setStatus(true);
        user.setOtp(null);
        userRepository.save(user);
        return "Email verified successfully!.";
    }


}
