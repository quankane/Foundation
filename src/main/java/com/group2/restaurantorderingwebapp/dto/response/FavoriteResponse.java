package com.group2.restaurantorderingwebapp.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteResponse {
    private long favoriteId;
    private DishResponse dish;
    private Long userId;
}
