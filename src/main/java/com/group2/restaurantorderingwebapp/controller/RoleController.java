package com.group2.restaurantorderingwebapp.controller;

import com.group2.restaurantorderingwebapp.dto.request.RoleRequest;
import com.group2.restaurantorderingwebapp.dto.response.ApiResponse;
import com.group2.restaurantorderingwebapp.service.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Role")
public class RoleController {
    private final RoleService roleService;

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping()
    public ResponseEntity<ApiResponse> createRole (@Valid @RequestBody RoleRequest roleRequest){
        ApiResponse apiResponse = ApiResponse.success(roleService.createRole(roleRequest));
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/name/{roleName}")
    public ResponseEntity<ApiResponse> getRoleByName(@PathVariable("roleName") String roleName){
        ApiResponse apiResponse = ApiResponse.success(roleService.getRoleByRoleName(roleName));
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping()
    public ResponseEntity<ApiResponse> getAllRole(){
        ApiResponse apiResponse = ApiResponse.success(roleService.getAllRole());
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{roleId}")
    public ResponseEntity<ApiResponse> deleteRoleByName(@PathVariable("roleId") Long roleId){
        ApiResponse apiResponse = ApiResponse.success(roleService.deleteRole(roleId));
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
}
