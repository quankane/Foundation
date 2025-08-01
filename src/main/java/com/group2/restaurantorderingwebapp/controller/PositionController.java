package com.group2.restaurantorderingwebapp.controller;

import com.group2.restaurantorderingwebapp.dto.request.PositionRequest;
import com.group2.restaurantorderingwebapp.dto.response.ApiResponse;
import com.group2.restaurantorderingwebapp.dto.response.PositionResponse;
import com.group2.restaurantorderingwebapp.service.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/positions")
@RequiredArgsConstructor
@Tag(name = "Positions")
public class PositionController {

    private final PositionService positionService;


    @Operation(summary = "Add Position", description = "Add Position API")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping()
    public ResponseEntity<ApiResponse> addPosition(@RequestBody PositionRequest positionRequest) {
        ApiResponse apiResponse = ApiResponse.success(positionService.createPosition(positionRequest));
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Get Position", description = "Get Position API")
    @GetMapping("/{positionId}")
    public ResponseEntity<ApiResponse> getById(@PathVariable("positionId") Long positionId) {
        PositionResponse positionResponse = positionService.getPosition(positionId);
        ApiResponse apiResponse = ApiResponse.success(positionResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get All Position", description = "Get All Position API")
    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        List<PositionResponse> positionResponses = positionService.getAllPositions();
        ApiResponse apiResponse = ApiResponse.success(positionResponses);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @Operation(summary = "Update Position", description = "Update Position API")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{positionId}")
    public ResponseEntity<ApiResponse> updateById(@PathVariable("positionId") Long positionId, @RequestBody PositionRequest positionRequest) {
        PositionResponse positionResponse = positionService.updatePosition(positionId, positionRequest);
        ApiResponse apiResponse = ApiResponse.success(positionResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary = "Delete Position", description = "Delete Position API")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{positionId}")
    public ResponseEntity<ApiResponse> deleteById(@PathVariable("positionId") Long positionId) {
        ApiResponse apiResponse = ApiResponse.success(positionService.deletePosition(positionId));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
