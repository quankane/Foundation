package com.group2.restaurantorderingwebapp.service.impl;

import com.group2.restaurantorderingwebapp.dto.request.UserRequest;
import com.group2.restaurantorderingwebapp.dto.response.PageCustom;
import com.group2.restaurantorderingwebapp.dto.response.UserResponse;
import com.group2.restaurantorderingwebapp.entity.Role;
import com.group2.restaurantorderingwebapp.entity.User;
import com.group2.restaurantorderingwebapp.exception.AppException;
import com.group2.restaurantorderingwebapp.exception.ErrorCode;
import com.group2.restaurantorderingwebapp.exception.ResourceNotFoundException;
import com.group2.restaurantorderingwebapp.repository.RoleRepository;
import com.group2.restaurantorderingwebapp.repository.UserRepository;
import com.group2.restaurantorderingwebapp.service.RedisService;
import com.group2.restaurantorderingwebapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;



@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final String KEY = "user";

    @Override
    public User createGuestUser(String username) {

       User user = new User();
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByRoleName("ROLE_GUEST").orElseThrow(()->new ResourceNotFoundException("role","role's name","ROLE_GUEST"));
        roles.add(role);
        user.setRoles(roles);
        user.setPhoneNumber("guest"+ LocalDateTime.now().toString());
        user.setPassword(passwordEncoder.encode("guest123"));
        user.setFirstName("Guest");
        user.setLastName("User");
        redisService.deleteAll(KEY);
        userRepository.save(user);

        return user;
    }

    @Override
    public UserResponse getUserById(Long userId) {
        String field = "userById:" + userId;
        var json = redisService.getHash(KEY, field);
        if (json==null){

        User user = userRepository.findByUserIdAndStatus(userId,true).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        UserResponse userResponse =  modelMapper.map(user, UserResponse.class);
        redisService.setHashRedis(KEY, field,redisService.convertToJson(userResponse));
        return userResponse;
        }
        redisService.flushAll();
        return redisService.convertToObject((String) json,UserResponse.class);
    }


    @Override
    public PageCustom<UserResponse> getAllUser(Pageable pageable) {
        String field = "allUser" + pageable.toString();
        var json = redisService.getHash(KEY,field);
        if (json == null){

        Page<User> page = userRepository.findAll(pageable);
        PageCustom<UserResponse> pageCustom = PageCustom.<UserResponse>builder()
                .pageNo(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .pageContent(page.getContent().stream().map(user->modelMapper.map(user, UserResponse.class)).toList())
                .build();
        redisService.setHashRedis(KEY,field,redisService.convertToJson(pageCustom));
        return pageCustom;
        }
        return (PageCustom<UserResponse>) redisService.convertToObject((String) json,PageCustom.class);
    }

    @Override
    public UserResponse updateUser(Long userId, UserRequest userRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        if (!userRequest.getPhoneNumber().equals(user.getPhoneNumber())){
            throw new AppException(ErrorCode.USER_EMAIL_OR_PHONE_CAN_NOT_CHANGE);
        }
        if (!userRequest.getEmail().equals(user.getEmail())){
            throw new AppException(ErrorCode.USER_EMAIL_OR_PHONE_CAN_NOT_CHANGE);
        }
        modelMapper.map(userRequest,user);
        redisService.deleteAll(KEY);
        return modelMapper.map(userRepository.save(user),UserResponse.class);
    }

    @Override
    public UserResponse changePassword(Long userId, String password) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setPassword(passwordEncoder.encode(password));
        redisService.deleteAll(KEY);
        return modelMapper.map(userRepository.save(user),UserResponse.class);
    }

    @Override
    public String deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        redisService.flushAll();
        userRepository.delete(user);
        return "User with id: " +userId+ " was deleted successfully";
    }
}


