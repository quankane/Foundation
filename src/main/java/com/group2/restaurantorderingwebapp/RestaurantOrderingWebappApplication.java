package com.group2.restaurantorderingwebapp;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
@Slf4j
public class RestaurantOrderingWebappApplication {

    @Value("${environment.info}")
    private String environmentInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantOrderingWebappApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(RestaurantOrderingWebappApplication.class, args);
    }

    @PostConstruct
    public void printEnvironmentInfo() {
        log.info("environmentInfo: " + environmentInfo);
    }

}
