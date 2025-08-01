package com.group2.restaurantorderingwebapp.repository;

import com.group2.restaurantorderingwebapp.entity.Category;
import com.group2.restaurantorderingwebapp.entity.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findAllByOrderByDishNameAsc();

    boolean existsByDishName(String dishName);

    List<Dish> findAllByCategories(Category category);

    Page<Dish> findAllByDishNameContainingIgnoreCase(String dishName, Pageable pageable);
}
