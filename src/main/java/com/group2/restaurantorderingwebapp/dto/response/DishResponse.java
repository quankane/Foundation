package com.group2.restaurantorderingwebapp.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DishResponse {
    private Long dishId;
    private String dishName;
    private String image;
    private String description;
    private Double price;
    private String status;
    private String ingredient;
    private int portion;
    private Long cookingTime;
    private int orderAmount;
    private int servedAmount;
    private double rankingAvg = 0.0;
    private Set<CategoryResponse > categories;
}
