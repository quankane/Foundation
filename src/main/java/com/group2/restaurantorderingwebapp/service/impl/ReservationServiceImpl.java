package com.group2.restaurantorderingwebapp.service.impl;

import com.group2.restaurantorderingwebapp.dto.request.ReservationRequest;
import com.group2.restaurantorderingwebapp.dto.response.PageCustom;
import com.group2.restaurantorderingwebapp.dto.response.ReservationResponse;
import com.group2.restaurantorderingwebapp.entity.*;
import com.group2.restaurantorderingwebapp.enumeration.ReservationStatus;
import com.group2.restaurantorderingwebapp.exception.AppException;
import com.group2.restaurantorderingwebapp.exception.ErrorCode;
import com.group2.restaurantorderingwebapp.exception.ResourceNotFoundException;
import com.group2.restaurantorderingwebapp.repository.*;
import com.group2.restaurantorderingwebapp.service.ReservationService;
import com.group2.restaurantorderingwebapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PositonRepository positonRepository;
    private final OrderRepository orderRepository;

    @Override
    public ReservationResponse create(ReservationRequest reservationRequest) {
        User user;
        if (reservationRequest.getUserId() != null) {
            user = userRepository.findById(reservationRequest.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", reservationRequest.getUserId()));
        } else {
            LocalDateTime now = LocalDateTime.now();
            user = userService.createGuestUser("guest" + now);
        }

      if(reservationRepository.existsReservationWithSameTimeSlot(reservationRequest.getOrderTime())) {
            throw new AppException(ErrorCode.RESERVATION_ALREADY_EXISTS);
      }
        if (reservationRequest.getQuantityTables() > 15) {
            throw new AppException(ErrorCode.RESERVATION_NOT_ENOUGH_TABLES);
        }

        Reservation reservation = modelMapper.map(reservationRequest, Reservation.class);
        Set<Order> orders = new HashSet<>();
        for (Long orderId : reservationRequest.getOrderIds()) {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
            orders.add(order);
        }
        reservation.setQuantityTables(reservationRequest.getQuantityTables());
        reservation.setUser(user);
        reservation.setOrders(orders);
        reservationRepository.save(reservation);
        return modelMapper.map(reservation, ReservationResponse.class);

    }

    @Override
    public String deleteById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", reservationId));
        reservationRepository.delete(reservation);
        return "Reservation successfully deleted";
    }

    @Override
    public PageCustom<ReservationResponse> getReservationByUser(Long userId, Pageable pageable) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("UserId", "id", userId));
        Page<Reservation> reservations = reservationRepository.findAllByUser(user, pageable);
        PageCustom<ReservationResponse> pageCustom = PageCustom.<ReservationResponse>builder()
                .pageNo(reservations.getNumber() + 1)
                .pageSize(reservations.getSize())
                .totalPages(reservations.getTotalPages())
                .pageContent(reservations.getContent().stream().map(reservation -> modelMapper.map(reservation, ReservationResponse.class)).toList())
                .build();
        return pageCustom;

    }

    // User confirm make a reservation
    @Override
    public String confirmReservationStatus(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));
        reservation.setStatus(ReservationStatus.RESERVATION_CONFIRMED.getText(2));
        reservationRepository.save(reservation);
        return "Make a reservation successfully";

    }





}
