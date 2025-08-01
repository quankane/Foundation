package com.group2.restaurantorderingwebapp.dto.request;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequest {
    private LocalDateTime orderTime;
    private boolean type;
    private String address;
    private String fullname;
    private String phone;
    private String note;
    private Long userId;
    private int quantityTables;
    private Set<Long> orderIds;
}
