package com.group2.restaurantorderingwebapp.service;

import com.group2.restaurantorderingwebapp.dto.request.UserRequest;
import com.group2.restaurantorderingwebapp.dto.response.ApiResponse;
import com.group2.restaurantorderingwebapp.dto.response.PageCustom;
import com.group2.restaurantorderingwebapp.dto.response.UserResponse;
import com.group2.restaurantorderingwebapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {



    User createGuestUser(String username);

    UserResponse getUserById(Long id);


    PageCustom<UserResponse> getAllUser(Pageable pageable);

    UserResponse updateUser(Long id, UserRequest userRequest);

    UserResponse changePassword(Long userId, String password);

    String deleteUser(Long id);
}
