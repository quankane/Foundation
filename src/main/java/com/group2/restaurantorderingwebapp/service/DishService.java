package com.group2.restaurantorderingwebapp.service;

import com.group2.restaurantorderingwebapp.dto.request.DishRequest;
import com.group2.restaurantorderingwebapp.dto.response.DishResponse;
import com.group2.restaurantorderingwebapp.dto.response.PageCustom;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DishService {


    DishResponse addDish(DishRequest dishRequest);

    PageCustom<DishResponse> getAllDishes(Pageable pageable);

    DishResponse getDishById(Long dishId);

    DishResponse updateDish(Long dishId, DishRequest dishRequest);

    String deleteDish(Long dishId);

    List<DishResponse> getDishesByCategory(String categoryName);

    PageCustom<DishResponse> searchDish(String dishName, Pageable pageable);
}
