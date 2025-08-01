package com.group2.restaurantorderingwebapp.service;

import com.group2.restaurantorderingwebapp.dto.request.PaymentRequest;
import com.group2.restaurantorderingwebapp.dto.response.PageCustom;
import com.group2.restaurantorderingwebapp.dto.response.PaymentResponse;
import com.group2.restaurantorderingwebapp.dto.response.VnPayResponse;
import com.group2.restaurantorderingwebapp.entity.Order;
import com.group2.restaurantorderingwebapp.entity.Payment;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentService {


    VnPayResponse createPayment(PaymentRequest paymentRequest, HttpServletRequest request);

    Payment payCallBackHandle(HttpServletRequest request);

    PaymentResponse getPaymentById(Long paymentId);

    PageCustom<PaymentResponse> getAllPayment(Pageable pageable);

    PageCustom<PaymentResponse> getPaymentByUser(Long userId, Pageable pageable);
}
