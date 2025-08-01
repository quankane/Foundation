package com.group2.restaurantorderingwebapp.service.impl;

import com.group2.restaurantorderingwebapp.dto.request.PositionRequest;
import com.group2.restaurantorderingwebapp.dto.response.OrderResponse;
import com.group2.restaurantorderingwebapp.dto.response.PositionResponse;
import com.group2.restaurantorderingwebapp.entity.Position;
import com.group2.restaurantorderingwebapp.exception.ResourceNotFoundException;
import com.group2.restaurantorderingwebapp.repository.PositonRepository;
import com.group2.restaurantorderingwebapp.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
  private final PositonRepository positionRepository;
    private final ModelMapper modelMapper;

    @Override
    public PositionResponse createPosition(PositionRequest positionRequest) {
        Position position = modelMapper.map(positionRequest, Position.class);
        return  modelMapper.map(positionRepository.save(position), PositionResponse.class);
    }


    @Override
    public PositionResponse getPosition(Long positionId) {
        Position position = positionRepository.findById(positionId).orElseThrow(() -> new ResourceNotFoundException("Position", "id", positionId));
        return modelMapper.map(position, PositionResponse.class);
    }

    @Override
    public List<PositionResponse> getAllPositions() {
        return positionRepository.findAll().stream()
                .map(position -> modelMapper.map(position, PositionResponse.class)
                )
                .collect(Collectors.toList());
    }

    @Override
    public PositionResponse updatePosition(Long positionId, PositionRequest positionRequest) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new ResourceNotFoundException("Position", "id", positionId));
        position.setPositionName(positionRequest.getPositionName());
        Position updatedPosition = positionRepository.save(position);
        return modelMapper.map(updatedPosition, PositionResponse.class);
    }

    @Override
    public String deletePosition(Long positionId) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new ResourceNotFoundException("Position", "id", positionId));

        positionRepository.delete(position);
        return "Deleted position successfully";

    }




}
