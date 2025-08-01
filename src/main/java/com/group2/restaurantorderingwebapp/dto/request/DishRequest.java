package com.group2.restaurantorderingwebapp.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DishRequest {
    @NotNull(message = "Dish name is required")
    @NotEmpty(message = "Dish name is required")
    private String dishName;

    @NotNull(message = "Image is required")
    @NotEmpty(message = "Image is required")
    private String image;

    private String description;
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Double price;
    @NotNull(message = "Ingredient is required")
    @NotEmpty(message = "Ingredient is required")
    private String ingredient;
    @Min(value = 1, message = "Portion must be greater than 1")
    private int portion;
    private Long cookingTime;
    private int servedAmount;
    private Set<String> categories;
}
