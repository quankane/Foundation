package com.group2.restaurantorderingwebapp.controller;

import com.group2.restaurantorderingwebapp.dto.request.CartRequest;
import com.group2.restaurantorderingwebapp.dto.request.OrderRequest;
import com.group2.restaurantorderingwebapp.dto.request.ReservationRequest;
import com.group2.restaurantorderingwebapp.dto.response.ApiResponse;
import com.group2.restaurantorderingwebapp.service.CartService;
import com.group2.restaurantorderingwebapp.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservation", description = "Reservation API")
public class ReservationController {
    private final ReservationService reservationService;

    @Operation(summary = "Create Reservation", description = "Create Reservation API")
    @PostMapping()
    public ResponseEntity<ApiResponse> createReservation(@RequestBody ReservationRequest reservationRequest) {
        ApiResponse apiResponse = ApiResponse.success(reservationService.create(reservationRequest));
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Get Reservation by userId", description = "View history")
//    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getOrderByCartId(@PathVariable("userId") Long userId,
                                                        @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
                                                        @RequestParam(value = "pageSize",defaultValue = "10", required = false) int pageSize,
                                                        @RequestParam(value = "sortBy",defaultValue = "createAt", required = false) String sortBy) {
        Pageable pageable = PageRequest.of(pageNo -1,pageSize, Sort.by(sortBy).ascending() );
        var response = reservationService.getReservationByUser(userId, pageable);

        ApiResponse apiResponse = ApiResponse.success(response);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PatchMapping("/{reservationId}/confirm-reservation-status")
//    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse> confirmReservationStatus(@PathVariable("reservationId") Long reservationId) {
        ApiResponse apiResponse = ApiResponse.success(reservationService.confirmReservationStatus(reservationId));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @Operation(summary = "Delete reservation", description = "Delete Reservation API")
//    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ApiResponse> deleteReservation(@PathVariable("reservationId") Long reservationId) {
        ApiResponse apiResponse = ApiResponse.success(reservationService.deleteById(reservationId));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


}
