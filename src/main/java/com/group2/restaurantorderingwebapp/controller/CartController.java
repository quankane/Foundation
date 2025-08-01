package com.group2.restaurantorderingwebapp.controller;

import com.group2.restaurantorderingwebapp.dto.request.CartRequest;
import com.group2.restaurantorderingwebapp.dto.request.CategoryRequest;
import com.group2.restaurantorderingwebapp.dto.response.ApiResponse;
import com.group2.restaurantorderingwebapp.service.CartService;
import com.group2.restaurantorderingwebapp.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Cart API")
public class CartController {
    private final CartService cartService;

    @Operation(summary = "Get Cart by Id", description = "Get Cart by Id")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{cartId}")
    public ResponseEntity<ApiResponse> getById (@PathVariable Long cartId){
        ApiResponse apiResponse = ApiResponse.success(cartService.getById(cartId));
        apiResponse.setCode(200);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary = "update Cart by Id", description = "Update Cart by Id")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{cartId}")
    public ResponseEntity<ApiResponse> updateById (@PathVariable Long cartId, @RequestBody @Valid CartRequest cartRequest){
        ApiResponse apiResponse = ApiResponse.success(cartService.updateById(cartId, cartRequest));
        apiResponse.setCode(200);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


}
