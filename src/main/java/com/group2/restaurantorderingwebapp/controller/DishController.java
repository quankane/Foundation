package com.group2.restaurantorderingwebapp.controller;

import com.group2.restaurantorderingwebapp.dto.request.DishRequest;
import com.group2.restaurantorderingwebapp.dto.response.ApiResponse;
import com.group2.restaurantorderingwebapp.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
@Tag(name = "Dish", description = "Dish API")
public class DishController {

    private final DishService dishService;

    @Operation(summary = "Add Dish", description = "Add Dish API")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping()
    public ResponseEntity<ApiResponse> addDish(@Valid @RequestBody DishRequest dishRequest){
        ApiResponse apiResponse = ApiResponse.success(dishService.addDish(dishRequest));
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Get Dish", description = "Get Dish API")
    @GetMapping("/{dishId}")
    public ResponseEntity<ApiResponse> getDishById(@PathVariable("dishId") Long dishId){

        ApiResponse apiResponse = ApiResponse.success(dishService.getDishById(dishId));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get Dish By Category", description = "Get Dish By Category API")
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<ApiResponse> getDishByCategory(@PathVariable("categoryName") String categoryName){

        ApiResponse apiResponse = ApiResponse.success(dishService.getDishesByCategory(categoryName));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get All Dishes", description = "Get All Dishes API")
    @GetMapping()
    public ResponseEntity<ApiResponse> getAllDish(
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "dishName", required = false) String sortBy
    ){
        Pageable pageable = PageRequest.of(pageNo -1,pageSize, Sort.by(sortBy).ascending() );
        ApiResponse apiResponse = ApiResponse.success(dishService.getAllDishes(pageable));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary = "Update Dish", description = "Update Dish API")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{dishId}")
    public ResponseEntity<ApiResponse> updateDish(@PathVariable("dishId") Long dishId, @Valid @RequestBody DishRequest dishRequest){
        ApiResponse apiResponse = ApiResponse.success(dishService.updateDish(dishId, dishRequest));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary = "Delete Dish", description = "Delete Dish API")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{dishId}")
    public ResponseEntity<ApiResponse> deleteDish(@PathVariable("dishId") Long dishId){
        ApiResponse apiResponse = ApiResponse.success(dishService.deleteDish(dishId));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchDish(
            @RequestParam ("name") String dishName,
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "dishName", required = false) String sortBy

    ){
        Pageable pageable = PageRequest.of(pageNo -1,pageSize, Sort.by(sortBy).ascending() );
        ApiResponse apiResponse = ApiResponse.success(dishService.searchDish(dishName, pageable));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
