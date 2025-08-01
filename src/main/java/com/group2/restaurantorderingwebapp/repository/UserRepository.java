package com.group2.restaurantorderingwebapp.repository;

import com.group2.restaurantorderingwebapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("from User u where u.phoneNumber=:username")
    Optional<User> findByPhoneNumber(String username);

//    @Query("select case when count(u) > 0 then true else false end from User u where u.phoneNumber = :emailOrPhone")
//    boolean existsByEmailOrPhone(String emailOrPhone);

    Optional<User> findByPhoneNumberOrEmail(String username, String username1);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    Optional<User> findByUserIdAndStatus(Long userId, boolean b);

    Optional<User> findByPhoneNumberOrEmailAndStatus(String emailOrPhone, String emailOrPhone1, boolean b);
}
