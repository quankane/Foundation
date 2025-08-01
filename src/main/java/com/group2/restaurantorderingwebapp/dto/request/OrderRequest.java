package com.group2.restaurantorderingwebapp.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;
    private Long cartId;
    private Long userId;
    @NotNull(message = "DishId cannot be null")
    private Long dishId;
    private Long positionId;
}
