package com.group2.restaurantorderingwebapp.service.impl;

import com.group2.restaurantorderingwebapp.dto.request.CategoryRequest;
import com.group2.restaurantorderingwebapp.dto.response.CategoryResponse;
import com.group2.restaurantorderingwebapp.entity.Category;
import com.group2.restaurantorderingwebapp.exception.AppException;
import com.group2.restaurantorderingwebapp.exception.ErrorCode;
import com.group2.restaurantorderingwebapp.exception.ResourceNotFoundException;
import com.group2.restaurantorderingwebapp.repository.CategoryRepository;
import com.group2.restaurantorderingwebapp.service.RedisService;
import com.group2.restaurantorderingwebapp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final RedisService redisService;
    private final String KEY = "category";

  @Override
    public CategoryResponse addCategory(CategoryRequest categoryRequest){
      if (categoryRepository.existsByCategoryName(categoryRequest.getCategoryName())){
          throw new AppException(ErrorCode.CATEGORY_EXISTED);
      }
      Category category = modelMapper.map(categoryRequest, Category.class);
      redisService.deleteAll(KEY);
      return modelMapper.map(categoryRepository.save(category), CategoryResponse.class);
  }

  @Override
    public CategoryResponse updateCategory(Long categoryId, CategoryRequest categoryRequest){
      if (categoryRepository.existsByCategoryName(categoryRequest.getCategoryName())){
          throw new AppException(ErrorCode.CATEGORY_EXISTED);
      }
      Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","id",categoryId));
      modelMapper.map(categoryRequest,category);
      redisService.deleteAll(KEY);
      return modelMapper.map(categoryRepository.save(category), CategoryResponse.class);
  }

  @Override
    public CategoryResponse getCategoryById(Long categoryId){
      String field = "categoryById:" + categoryId;
      var json  = redisService.getHash(KEY,field);
      if (json == null){
         Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","id",categoryId));
         CategoryResponse response = modelMapper.map(category, CategoryResponse.class);
         redisService.setHashRedis(KEY,field, redisService.convertToJson(response));
         return response;
      }
      return redisService.convertToObject((String) json, CategoryResponse.class);
  }

    @Override
    public List<CategoryResponse> getAllCategory() {
        String field = "allCategory";
        var json = redisService.getHash(KEY, field);
        if (json == null) {

            List<CategoryResponse> categories = categoryRepository.findAll().stream().map(category -> modelMapper.map(category, CategoryResponse.class)).toList();
            redisService.setHashRedis(KEY, field, redisService.convertToJson(categories));
            return categories;
        }
        return (List<CategoryResponse>) redisService.convertToObject((String) json, List.class);

  }

    @Override
    public String deleteCategory(Long categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","id",categoryId));
        categoryRepository.delete(category);
        redisService.flushAll();
        return "Category with id: " +categoryId+ " was deleted successfully";
    }

    @Override
    public CategoryResponse getCategoryByCategoryName(String categoryName){
        String field = "categoryByCategoryName:" + categoryName;
        var json  = redisService.getHash(KEY,field);
        if (json == null){

        Category categories = categoryRepository.findByCategoryName(categoryName).orElseThrow(()->new ResourceNotFoundException("Category","name",categoryName));
        CategoryResponse categoryResponse =  modelMapper.map(categories, CategoryResponse.class);
        redisService.setHashRedis(KEY,field,redisService.convertToJson(categoryResponse));
        return categoryResponse;
        }
        return redisService.convertToObject((String) json, CategoryResponse.class);


    }
}
