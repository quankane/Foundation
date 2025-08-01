package com.group2.restaurantorderingwebapp.service.impl;

import com.group2.restaurantorderingwebapp.dto.request.CartRequest;
import com.group2.restaurantorderingwebapp.dto.response.CartResponse;
import com.group2.restaurantorderingwebapp.entity.Cart;
import com.group2.restaurantorderingwebapp.exception.ResourceNotFoundException;
import com.group2.restaurantorderingwebapp.repository.CartRepository;
import com.group2.restaurantorderingwebapp.service.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
   private final CartRepository cartRepository;
   private final ModelMapper modelMapper;

   @Override
   public CartResponse getById(Long id){
      return modelMapper.map(cartRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("cart","id",id)),CartResponse.class);
   }

   @Override
   public CartResponse updateById(Long id, CartRequest cartRequest){
      Cart cart = cartRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("cart","id",id));
      modelMapper.map(cartRequest,cart);
      cartRepository.save(cart);
      return modelMapper.map(cart,CartResponse.class);
   }

}
