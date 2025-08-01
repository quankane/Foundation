package com.group2.restaurantorderingwebapp.service.impl;

import com.group2.restaurantorderingwebapp.dto.request.FavoriteRequest;
import com.group2.restaurantorderingwebapp.dto.response.DishResponse;
import com.group2.restaurantorderingwebapp.dto.response.FavoriteResponse;
import com.group2.restaurantorderingwebapp.dto.response.PageCustom;
import com.group2.restaurantorderingwebapp.entity.Dish;
import com.group2.restaurantorderingwebapp.entity.Favorite;
import com.group2.restaurantorderingwebapp.entity.User;
import com.group2.restaurantorderingwebapp.exception.AppException;
import com.group2.restaurantorderingwebapp.exception.ErrorCode;
import com.group2.restaurantorderingwebapp.exception.ResourceNotFoundException;
import com.group2.restaurantorderingwebapp.repository.DishRepository;
import com.group2.restaurantorderingwebapp.repository.FavoriteRepository;
import com.group2.restaurantorderingwebapp.repository.UserRepository;
import com.group2.restaurantorderingwebapp.service.FavoriteService;
import com.group2.restaurantorderingwebapp.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl  implements FavoriteService {

    private final RedisService redisService;
    private final String KEY = "favorite";
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final FavoriteRepository favoriteRepository;
    private final ModelMapper modelMapper;

    @Override
    public FavoriteResponse createFavorite(FavoriteRequest favoriteRequest) {
        if (favoriteRepository.existsByDishIdAndUserId(favoriteRequest.getDishId(), favoriteRequest.getUserId())) {
            throw new AppException(ErrorCode.FAVORITE_EXISTED);
        }
        User user = userRepository.findById(favoriteRequest.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", favoriteRequest.getUserId()));
        Dish dish = dishRepository.findById(favoriteRequest.getDishId()).orElseThrow(() -> new ResourceNotFoundException("Dish", "id", favoriteRequest.getDishId()));
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setDish(dish);
        redisService.deleteAll(KEY);
        favorite = favoriteRepository.save(favorite);
        return FavoriteResponse.builder()
                .favoriteId(favorite.getFavoriteId())
                .userId(favorite.getUser().getUserId())
                .dish(modelMapper.map(dish, DishResponse.class))
                .build();

    }

    @Override
    public String deleteFavorite(Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(() -> new ResourceNotFoundException("Favorite", "id", favoriteId));
        favoriteRepository.delete(favorite);
        redisService.deleteAll(KEY);
        return "Favorite with id: " +favoriteId+ " was deleted successfully";
    }

    @Override
    public PageCustom<FavoriteResponse> getFavoriteByUser(Long userId, Pageable pageable) {
        String field = "getFavoriteByUser"+userId+pageable.toString();
        var json = redisService.getHash(KEY, field);
        if (json == null) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
            Page<Favorite> page = favoriteRepository.findAllByUser(user, pageable);
            PageCustom<FavoriteResponse> pageCustom = PageCustom.<FavoriteResponse>builder()
                    .pageNo(page.getNumber() + 1)
                    .pageSize(page.getSize())
                    .totalPages(page.getTotalPages())
                    .pageContent(page.getContent().stream().map(favorite -> FavoriteResponse.builder()
                                .favoriteId(favorite.getFavoriteId())
                                .userId(favorite.getUser().getUserId())
                                .dish(modelMapper.map(favorite.getDish(), DishResponse.class))
                                .build()).toList())
                    .build();
            redisService.setHashRedis(KEY, field, redisService.convertToJson(pageCustom));
            return pageCustom;
        }
        return redisService.convertToObject((String) json, PageCustom.class);
    }



}
