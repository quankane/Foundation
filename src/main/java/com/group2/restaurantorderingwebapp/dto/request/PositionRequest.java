package com.group2.restaurantorderingwebapp.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PositionRequest {
    @NotEmpty(message = "Position name is required")
    @NotNull(message = "Position name is required")
    private String positionName;
}
