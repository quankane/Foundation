package com.group2.restaurantorderingwebapp.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {

    private Long reservationId;

    private String status;

    private LocalDateTime orderTime;

    private boolean type;

    private String address;

    private String fullname;

    private String phone;

    private String note;

    private Double totalPrice;

    private Set<UserResponse> users;

    private Set<PositionResponse> positions;

    private Set<OrderResponse> orders;
}
