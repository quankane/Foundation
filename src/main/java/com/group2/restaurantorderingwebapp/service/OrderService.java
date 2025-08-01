package com.group2.restaurantorderingwebapp.service;

import com.group2.restaurantorderingwebapp.dto.request.OrderRequest;
import com.group2.restaurantorderingwebapp.dto.response.OrderResponse;
import com.group2.restaurantorderingwebapp.dto.response.PageCustom;
import org.springframework.data.domain.Pageable;

public interface OrderService  {



    OrderResponse createOrder(OrderRequest orderRequest);

    String updatePaymentStatus(Long id);

    String confirmOrderStatus(Long id);

    String updateOrderStatus(Long id, String status);

    String updateRatingStatus(Long id);

    String updateOrderUser(Long id, Long userId);

    OrderResponse getOrderById(Long id);

    PageCustom<OrderResponse> getAllOrders(Pageable pageable);

    String deleteOrder(Long id);


    PageCustom<OrderResponse> getOrdersByPosition(Long positionId, Pageable pageable);

    PageCustom<OrderResponse> getOrdersByUser(Long userId, Pageable pageable);

    PageCustom<OrderResponse> getOrdersByCart(Long positionId, Pageable pageable);
}
