package com.group2.restaurantorderingwebapp.service;

import com.group2.restaurantorderingwebapp.dto.request.CategoryRequest;
import com.group2.restaurantorderingwebapp.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {


    CategoryResponse addCategory(CategoryRequest categoryRequest);

    CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest);

    CategoryResponse getCategoryById(Long id);

    List<CategoryResponse> getAllCategory();

    String deleteCategory(Long id);

   CategoryResponse getCategoryByCategoryName(String categoryName);
}
