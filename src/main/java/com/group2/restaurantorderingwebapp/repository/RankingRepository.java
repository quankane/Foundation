package com.group2.restaurantorderingwebapp.repository;

import com.group2.restaurantorderingwebapp.entity.Dish;
import com.group2.restaurantorderingwebapp.entity.Ranking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RankingRepository extends JpaRepository<Ranking,Long> {



    @Query("from Ranking r where r.dish.dishId=:id")
    Page<Ranking> findAllByDishId(Long id, Pageable pageable);


    @Query("from Ranking r where r.dish.dishId=:dishId and r.rankingStars=:rankingStars")
    Page<Ranking> findAllByDishIdRankingStars(Long dishId, int rankingStars, Pageable pageable);


    @Query("from Ranking r where r.dish.dishId=:dishId and r.user.userId=:userId")
    Page<Ranking> findAllByDishIdUserId(Long dishId, Long userId, Pageable pageable);

    List<Ranking> findAllByDish(Dish dish);
}
