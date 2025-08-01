package com.group2.restaurantorderingwebapp.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RankingRequest {

    @NotNull(message = "Comment is required")
    @NotEmpty(message = "Comment is required")
    private String comment;
    @NotNull(message = "Ranking stars is required")
    private int rankingStars;
    @NotNull(message = "Dish ID is required")
    private Long dishID;
    @NotNull
    private Long userId;


}
