package com.group2.restaurantorderingwebapp.repository;

import com.group2.restaurantorderingwebapp.entity.Favorite;
import com.group2.restaurantorderingwebapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    Page<Favorite> findAllByUser(User user, Pageable pageable);

    @Query("select case when count(f) > 0 then true else false end from Favorite f where f.dish.dishId=:dishId and f.user.userId=:userId")
    boolean existsByDishIdAndUserId(Long dishId, Long userId);
}
