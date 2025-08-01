package com.group2.restaurantorderingwebapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dishes")
public class Dish extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dishId;
    @Column(nullable = false)
    private String dishName;
    @Column(columnDefinition = "TEXT",nullable = false)
    private String image;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String ingredient;
    private int portion;
    private Long cookingTime;
    @Column(nullable = false)
    private Double price;
    private String status="Out of stock";
    private int orderAmount = 0;
    private int servedAmount = 0;
    private double rankingAvg;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(
            name = "dish_category",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    @OneToMany(mappedBy = "dish",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Ranking> rankings;

    @JsonIgnore
    @OneToMany(mappedBy = "dish",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Favorite> favorites;

}
