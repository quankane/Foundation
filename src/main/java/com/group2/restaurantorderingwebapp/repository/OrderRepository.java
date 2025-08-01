package com.group2.restaurantorderingwebapp.repository;

import com.group2.restaurantorderingwebapp.entity.Cart;
import com.group2.restaurantorderingwebapp.entity.Order;
import com.group2.restaurantorderingwebapp.entity.Position;
import com.group2.restaurantorderingwebapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


    Page<Order> findAllByPosition(Position position, Pageable pageable);

    Page<Order> findAllByUser(User user, Pageable pageable);

    Page<Order> findAllByPositionAndStatus(Position position, boolean b, Pageable pageable);

    @Query("select case when count(o) > 0 then true else false end from Order o where o.dish.dishId=:dishId and o.position.positionId=:positionId and o.status=:b")
    boolean existsByDishAndPositionAndStatus(Long dishId, Long positionId, boolean b);

    @Query("from Order o where o.dish.dishId=:dishId and o.position.positionId=:positionId and o.status=:b")
    Optional<Order> findByDishAndPositionAndStatus(Long dishId, Long positionId, boolean b);


    Optional<Order> findByOrderIdAndStatus(Long orderIds, boolean b);

    Page<Order> findAllByUserAndStatus(User user, boolean b, Pageable pageable);

    @Query("select case when count(o) > 0 then true else false end from Order o where o.dish.dishId=:dishId and o.position.positionId=:positionId and o.orderStatus=:considering")
    boolean existsByDishAndPositionAndOrderStatus(Long dishId, Long positionId, String considering);

    @Query("from Order o where o.dish.dishId=:dishId and o.cart.cartId =:cartId and o.status=:b")
    Optional<Order> findByDishAndCartAndStatus(Long dishId, Long cartId, boolean b);

    Page<Order> findAllByCartAndStatus(Cart cart, boolean b, Pageable pageable);
}
