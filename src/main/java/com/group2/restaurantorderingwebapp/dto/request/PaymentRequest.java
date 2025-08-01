package com.group2.restaurantorderingwebapp.dto.request;

import com.group2.restaurantorderingwebapp.entity.Order;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    private String paymentMethod;
    private Long userId;
    private List<Long> orderIds;
}
