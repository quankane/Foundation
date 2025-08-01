package com.group2.restaurantorderingwebapp.repository;

import com.group2.restaurantorderingwebapp.entity.Cart;
import com.group2.restaurantorderingwebapp.entity.Reservation;
import com.group2.restaurantorderingwebapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {


    Page<Reservation> findAllByUser(User user, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Reservation r " +
            "WHERE DATE(r.orderTime) = DATE(:orderTime) " +
            "AND ((HOUR(r.orderTime) BETWEEN 7 AND 11 AND HOUR(:orderTime) BETWEEN 7 AND 11) " +
            "OR (HOUR(r.orderTime) BETWEEN 12 AND 17 AND HOUR(:orderTime) BETWEEN 12 AND 17) " +
            "OR (HOUR(r.orderTime) BETWEEN 18 AND 21 AND HOUR(:orderTime) BETWEEN 18 AND 21))")
    boolean existsReservationWithSameTimeSlot(@Param("orderTime") LocalDateTime orderTime);
}
