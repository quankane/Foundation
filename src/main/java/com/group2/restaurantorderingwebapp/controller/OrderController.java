package com.group2.restaurantorderingwebapp.controller;

import com.group2.restaurantorderingwebapp.dto.request.OrderRequest;
import com.group2.restaurantorderingwebapp.dto.response.ApiResponse;
import com.group2.restaurantorderingwebapp.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Order API")
public class OrderController {

    private final OrderService orderService;
    private final SimpMessagingTemplate messagingTemplate;

    @Operation(summary = "Add Order", description = "Add Order API")
    @PostMapping()
    public ResponseEntity<ApiResponse> addOrder(@RequestBody OrderRequest orderRequest) {
        ApiResponse apiResponse = ApiResponse.success(orderService.createOrder(orderRequest));
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }


    @Operation(summary = "Get All Order", description = "Get All Order API")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping()
    public ResponseEntity<ApiResponse> getAllOrder(
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "createAt", required = false) String sortBy
    ) {
        Pageable pageable = PageRequest.of(pageNo -1,pageSize, Sort.by(sortBy).ascending() );
        ApiResponse apiResponse = ApiResponse.success(orderService.getAllOrders(pageable));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get Order By Id", description = "Get Order By Id API")
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long orderId) {
        ApiResponse apiResponse = ApiResponse.success(orderService.getOrderById(orderId));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary = "Update Order Status", description = "Update Order Status API")
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/{orderId}/update-pay-status")
    public ResponseEntity<ApiResponse> updatePayStatus(@PathVariable("orderId") Long orderId) {
        ApiResponse apiResponse = ApiResponse.success(orderService.updatePaymentStatus(orderId));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PatchMapping("/{orderId}/confirm-order-status")
    public ResponseEntity<ApiResponse> confirmOrderStatus(@PathVariable("orderId") Long orderId) {
        ApiResponse apiResponse = ApiResponse.success(orderService.confirmOrderStatus(orderId));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PatchMapping("/{orderId}/update-order-status")
    public ResponseEntity<ApiResponse> updateOrderStatus(@PathVariable("orderId") Long orderId,@RequestParam("status") String status) {
        ApiResponse apiResponse = ApiResponse.success(orderService.updateOrderStatus(orderId,status));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PatchMapping("/{orderId}/update-rating-status")
    public ResponseEntity<ApiResponse> updateRatingStatus(@PathVariable("orderId") Long orderId) {
        ApiResponse apiResponse = ApiResponse.success(orderService.updateRatingStatus(orderId));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary = "Update Order User", description = "Update Order User API")
    @PatchMapping("/{orderId}/update-user")
    public ResponseEntity<ApiResponse> updateOrderUser(@PathVariable Long orderId, @RequestParam("user") Long userId) {
        ApiResponse apiResponse = ApiResponse.success(orderService.updateOrderUser(orderId, userId));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary = "Delete Order", description = "Delete Order API")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse> deleteOrder(@PathVariable Long orderId) {
        ApiResponse apiResponse = ApiResponse.success(orderService.deleteOrder(orderId));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get Order By User Id", description = "Get Order By User Id API")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getOrderByUserId(@PathVariable("userId") Long userId,
                                                        @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
                                                        @RequestParam(value = "pageSize",defaultValue = "10", required = false) int pageSize,
                                                        @RequestParam(value = "sortBy",defaultValue = "ratingStatus", required = false) String sortBy) {
        Pageable pageable = PageRequest.of(pageNo -1,pageSize, Sort.by(sortBy).ascending() );
        ApiResponse apiResponse = ApiResponse.success(orderService.getOrdersByUser(userId,pageable));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get Order By Position Id", description = "Get Order By Position Id API")
    @GetMapping("/position/{positionId}")
    public ResponseEntity<ApiResponse> getOrderByPositionId(@PathVariable("positionId") Long positionId,
                                                            @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
                                                            @RequestParam(value = "pageSize",defaultValue = "10", required = false) int pageSize,
                                                            @RequestParam(value = "sortBy",defaultValue = "createAt", required = false) String sortBy) {
        Pageable pageable = PageRequest.of(pageNo -1,pageSize, Sort.by(sortBy).ascending() );
        var response = orderService.getOrdersByPosition(positionId,pageable);
        messagingTemplate.convertAndSend("/topic/order", response);
        ApiResponse apiResponse = ApiResponse.success(response);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get Order By Cart Id", description = "Get Order By Cart Id API")
    @GetMapping("/cart/{cartId}")
    public ResponseEntity<ApiResponse> getOrderByCartId(@PathVariable("cartId") Long cartId,
                                                            @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
                                                            @RequestParam(value = "pageSize",defaultValue = "10", required = false) int pageSize,
                                                            @RequestParam(value = "sortBy",defaultValue = "createAt", required = false) String sortBy) {
        Pageable pageable = PageRequest.of(pageNo -1,pageSize, Sort.by(sortBy).ascending() );
        var response = orderService.getOrdersByCart(cartId,pageable);
        System.out.println("response: " + response.getPageContent());
        ApiResponse apiResponse = ApiResponse.success(response);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
