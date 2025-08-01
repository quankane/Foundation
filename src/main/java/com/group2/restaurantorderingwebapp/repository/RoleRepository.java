package com.group2.restaurantorderingwebapp.repository;

import com.group2.restaurantorderingwebapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

   Optional<Role>findByRoleName(String roleName);
}
