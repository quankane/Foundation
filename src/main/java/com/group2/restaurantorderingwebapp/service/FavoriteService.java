package com.group2.restaurantorderingwebapp.service;

import com.group2.restaurantorderingwebapp.dto.request.FavoriteRequest;
import com.group2.restaurantorderingwebapp.dto.response.FavoriteResponse;
import com.group2.restaurantorderingwebapp.dto.response.PageCustom;
import org.springframework.data.domain.Pageable;

public interface FavoriteService {
    FavoriteResponse createFavorite(FavoriteRequest favoriteRequest);

    String deleteFavorite(Long favoriteId);

    PageCustom<FavoriteResponse> getFavoriteByUser(Long userId, Pageable pageable);
}
