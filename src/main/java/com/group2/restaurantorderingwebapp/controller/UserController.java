package com.group2.restaurantorderingwebapp.controller;

import com.group2.restaurantorderingwebapp.dto.request.UserRequest;
import com.group2.restaurantorderingwebapp.dto.response.ApiResponse;
import com.group2.restaurantorderingwebapp.dto.response.UserResponse;
import com.group2.restaurantorderingwebapp.service.UserService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
public class UserController {

    private final UserService useService;

    @Operation(summary = "Get All User", description = "Get All User API")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping()
    public ResponseEntity<ApiResponse> getAllUser(
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "createAt",required = false) String sortBy

    ){
        Pageable pageable = PageRequest.of(pageNo -1,pageSize, Sort.by(sortBy).ascending() );
        ApiResponse apiResponse = ApiResponse.success(useService.getAllUser(pageable));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get User By Id", description = "Get User By Id API")
      @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable("userId") Long userId){
        ApiResponse apiResponse = ApiResponse.success(useService.getUserById(userId));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @Operation(summary = "Add User", description = "Add User API")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable("userId") Long userId, @Valid @RequestBody UserRequest userRequest){
        ApiResponse apiResponse = ApiResponse.success(useService.updateUser(userId, userRequest));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @Operation(summary = "Delete User", description = "Delete User API")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Long userId){
        ApiResponse apiResponse = ApiResponse.success(useService.deleteUser(userId));
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }


}
