package com.group2.restaurantorderingwebapp.dto.request;

import com.group2.restaurantorderingwebapp.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    @Size(min = 3,max = 20,message = "First name should be between 3 and 20 characters")
    private String firstName;
    @Size(min = 3,max = 20,message = "Last name should be between 3 and 20 characters")
    private String lastName;
    @Builder.Default
    private String imageUrl = "https://images.squarespace-cdn.com/content/v1/54b7b93ce4b0a3e130d5d232/1519987020970-8IQ7F6Z61LLBCX85A65S/icon.png?format=1000w";
    @Size(min =10,message = "phone should be at least 10 characters")
    private String email;
    private String phoneNumber;
    private String address;
    private String gender;
    private LocalDate Dob;
}
