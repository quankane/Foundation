package com.group2.restaurantorderingwebapp.service;

import com.group2.restaurantorderingwebapp.dto.request.CartRequest;
import com.group2.restaurantorderingwebapp.dto.request.CategoryRequest;
import com.group2.restaurantorderingwebapp.dto.response.CartResponse;
import com.group2.restaurantorderingwebapp.dto.response.CategoryResponse;

import java.util.List;

public interface CartService {


    CartResponse getById(Long id);

    CartResponse updateById(Long id, CartRequest cartRequest);
}
