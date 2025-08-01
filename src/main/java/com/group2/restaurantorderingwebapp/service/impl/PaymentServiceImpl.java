package com.group2.restaurantorderingwebapp.service.impl;

import com.group2.restaurantorderingwebapp.config.VnPayConfig;
import com.group2.restaurantorderingwebapp.dto.request.PaymentRequest;
import com.group2.restaurantorderingwebapp.dto.response.*;
import com.group2.restaurantorderingwebapp.entity.Dish;
import com.group2.restaurantorderingwebapp.entity.Order;
import com.group2.restaurantorderingwebapp.entity.Payment;
import com.group2.restaurantorderingwebapp.entity.User;
import com.group2.restaurantorderingwebapp.exception.AppException;
import com.group2.restaurantorderingwebapp.exception.ErrorCode;
import com.group2.restaurantorderingwebapp.repository.DishRepository;
import com.group2.restaurantorderingwebapp.repository.OrderRepository;
import com.group2.restaurantorderingwebapp.repository.PaymentRepository;
import com.group2.restaurantorderingwebapp.repository.UserRepository;
import com.group2.restaurantorderingwebapp.service.PaymentService;
import com.group2.restaurantorderingwebapp.service.RedisService;
import com.group2.restaurantorderingwebapp.service.UserService;
import com.group2.restaurantorderingwebapp.util.VnPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final UserService userService;

    private final VnPayConfig vnPayConfig;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final PaymentRepository paymentRepository;
    private final RedisService redisService;
    private final String KEY = "payment";

    Map<Integer, List<Order>> orderMap = new HashMap<>();
    Map<Integer, User> userMap = new HashMap<>();

    @Override
    public VnPayResponse createPayment(PaymentRequest paymentRequest, HttpServletRequest request) {
        long sumAmount = 0;
        List<Order> orders = new ArrayList<>();
        for (Long orderId : paymentRequest.getOrderIds()) {
            Order order = orderRepository.findByOrderIdAndStatus(orderId, false).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
            orders.add(order);
            sumAmount += order.getTotalPrice();
        }

        long amount = sumAmount * 24000L * 100L;

        String vnp_TxnRef = VnPayUtil.getRandomNumber(8);
        orderMap.put(Integer.valueOf(vnp_TxnRef), orders);

        User user = new User();
        if (paymentRequest.getUserId() != null) {
            user = userRepository.findById(paymentRequest.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        }
        else {
            LocalDateTime now = LocalDateTime.now();
            user = userService.createGuestUser("guest" + now);
        }

        userMap.put(Integer.valueOf(vnp_TxnRef), user);

        String bankCode = request.getParameter("bankCode");
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        vnpParamsMap.put("vnp_TxnRef", vnp_TxnRef);
        if (bankCode != null && !bankCode.isEmpty()) {

            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VnPayUtil.getIpAddress(request));
        // Build query url
        String queryUrl = VnPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VnPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnSecureHash = VnPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnSecureHash;
        String paymentURl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return VnPayResponse.builder()
                .code(200)
                .message("Success")
                .paymentUrl(paymentURl)
                .build();
    }

    @Override
    public Payment payCallBackHandle(HttpServletRequest request) {


        String vnp_TxnRef = request.getParameter("vnp_TxnRef");

        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");

        String amount = request.getParameter("vnp_Amount");

        if (!vnp_ResponseCode.equals("00")) {
            throw new AppException(ErrorCode.PAYMENT_FAIL);

        }
        List<Order> orders = orderMap.get(Integer.valueOf(vnp_TxnRef));
        Payment payment = new Payment();
        payment.setPaymentMethod("Vn-Pay");
        if (orders != null) {
            List<Order> saveOrders = new ArrayList<>();
            for (Order order : orders) {

                Order existingOrder = orderRepository.findByOrderIdAndStatus(order.getOrderId(), false).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
                Dish dish = existingOrder.getDish();
                dish.setOrderAmount(dish.getOrderAmount() + existingOrder.getQuantity());
                dishRepository.save(dish);

                existingOrder.setStatus(true);
                existingOrder = orderRepository.save(existingOrder);
                saveOrders.add(existingOrder);
            }
            payment.setOrder(saveOrders);
        }
        User user = userMap.get(Integer.valueOf(vnp_TxnRef));

        payment.setAmount(Long.valueOf(amount));
        user = userRepository.findById(user.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        payment.setUser(user);

        payment = paymentRepository.save(payment);

        userMap.remove(Integer.valueOf(vnp_TxnRef));
        orderMap.remove(Integer.valueOf(vnp_TxnRef));


        redisService.deleteAll(KEY);
        return payment;
    }

    @Override
    public PaymentResponse getPaymentById(Long paymentId) {
        String field = "paymentById"+ paymentId;
        var json = redisService.getHash(KEY,field);
        if (json == null){

        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_EXISTED));
        PaymentResponse paymentResponse = modelMapper.map(payment, PaymentResponse.class);
        paymentResponse.setOrders(payment.getOrder().stream().map(order -> modelMapper.map(order, OrderResponse.class)).collect(Collectors.toList()));
        redisService.setHashRedis(KEY,field,redisService.convertToJson(paymentResponse));
        return paymentResponse;
        }
        return redisService.convertToObject((String) json,PaymentResponse.class);
    }

    @Override
    public PageCustom<PaymentResponse> getAllPayment(Pageable pageable) {
        String field = "allPayment"+pageable.toString();
        var json = redisService.getHash(KEY,field);
        if (json == null)
        {

        Page<Payment> page = paymentRepository.findAll(pageable);
        PageCustom<PaymentResponse> pageCustom = PageCustom.<PaymentResponse>builder()
                .pageNo(page.getNumber() + 1)
                .pageSize(page.getSize())
                .pageContent(page.getContent().stream().map(payment ->
                        PaymentResponse.builder().paymentId(payment.getPaymentId())
                                .paymentMethod(payment.getPaymentMethod())
                                .user(modelMapper.map(payment.getUser(), UserResponse.class))
                                .amount(payment.getAmount())
                                .orders(payment.getOrder().stream().map(order -> modelMapper.map(order, OrderResponse.class)).collect(Collectors.toList()))
                                .build()
                ).toList())
                .totalPages(page.getTotalPages())
                .build();
        redisService.setHashRedis(KEY,field,redisService.convertToJson(pageCustom));
        return pageCustom;
        }
        return redisService.convertToObject((String) json,PageCustom.class);

    }

    @Override
    public PageCustom<PaymentResponse> getPaymentByUser(Long userId, Pageable pageable) {
        String field = "paymentByUser"+userId+pageable.toString();
        var json = redisService.getHash(KEY,field);
        if (json == null){

        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Page<Payment> page = paymentRepository.findAllByUser(user, pageable);
        PageCustom<PaymentResponse> pageCustom = PageCustom.<PaymentResponse>builder()
                .pageNo(page.getNumber() + 1)
                .pageSize(page.getSize())
                .pageContent(page.getContent().stream().map(payment -> modelMapper.map(payment, PaymentResponse.class)).toList())
                .totalPages(page.getTotalPages())
                .build();
        redisService.setHashRedis(KEY,field,redisService.convertToJson(pageCustom));
        return pageCustom;
        }
        return redisService.convertToObject((String) json,PageCustom.class);
    }


}
