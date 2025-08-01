package com.group2.restaurantorderingwebapp.service;

import com.group2.restaurantorderingwebapp.dto.request.PositionRequest;
import com.group2.restaurantorderingwebapp.dto.response.PositionResponse;

import java.util.List;

public interface PositionService {


    PositionResponse createPosition(PositionRequest positionRequest);

    PositionResponse getPosition(Long id);

    List<PositionResponse> getAllPositions();

    PositionResponse updatePosition(Long id, PositionRequest positionRequest);


    String deletePosition(Long id);
}
