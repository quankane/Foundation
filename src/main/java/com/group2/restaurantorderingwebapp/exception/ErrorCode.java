package com.group2.restaurantorderingwebapp.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_ERROR(500, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "User already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1002, "User does not exist", HttpStatus.NOT_FOUND),
    USER_UNAUTHENTICATED(1003, "User is not authenticated", HttpStatus.UNAUTHORIZED),
    USER_LOGGED_OUT(1004, "User is logged out", HttpStatus.UNAUTHORIZED),
    USER_TOKEN_INCORRECT(1005, "User token is incorrect", HttpStatus.FORBIDDEN),

    EMAIL_EXISTED(1006, "Email already exists", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND(1007, "Email not found", HttpStatus.NOT_FOUND),
    EMAIL_CAN_NOT_UPDATE(1008, "Email can not update", HttpStatus.BAD_REQUEST),

    PHONE_EXISTED(1009, "Phone already exists", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_CAN_NOT_UPDATE(1010, "Phone number can not update", HttpStatus.BAD_REQUEST),

    USERNAME_EXISTED(1011, "Username already exists", HttpStatus.BAD_REQUEST),
    USER_ALREADY_EXISTS(1012, "User already exists", HttpStatus.BAD_REQUEST),
    USER_EMAIL_OR_PHONE_CAN_NOT_CHANGE(1013, "User email or phone can not change", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(1014, "Token expired", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD(1015, "Wrong password", HttpStatus.BAD_REQUEST),
    WRONG_OTP(1016, "Wrong OTP", HttpStatus.BAD_REQUEST),
    USER_NOT_VERIFIED(1017, "User not verified", HttpStatus.BAD_REQUEST),



    ORDER_NOT_EXISTED(2001, "Order does not exist", HttpStatus.NOT_FOUND),
    RESERVATION_NOT_ENOUGH_TABLES(2003, "Not enough available tables ", HttpStatus.NOT_FOUND),
    ORDER_REACHED_MAX_QUANTITY(7002, "Order reached max quantity", HttpStatus.BAD_REQUEST),
    POSITION_OR_CART_NOT_FOUND(2002, "Position or Cart not found", HttpStatus.NOT_FOUND),

    CATEGORY_NOT_EXISTED(3001, "Category does not exist", HttpStatus.NOT_FOUND),
    CATEGORY_EXISTED(3002, "Category already exists", HttpStatus.BAD_REQUEST),

    DISH_NOT_EXISTED(4001, "Dish does not exist", HttpStatus.NOT_FOUND),
    DISH_EXISTED(4002, "Dish already exists", HttpStatus.BAD_REQUEST),

    COMMENT_NOT_EXISTED(5001, "Comment does not exist", HttpStatus.NOT_FOUND),

    PAYMENT_FAIL(5002, "Payment fail", HttpStatus.BAD_REQUEST),
    PAYMENT_NOT_EXISTED(5003, "Payment does not exist", HttpStatus.NOT_FOUND),

    FAVORITE_EXISTED(6001, "Favorite already exists", HttpStatus.BAD_REQUEST),

    QUANTITY_NOT_ENOUGH(7001, "Quantity not enough", HttpStatus.BAD_REQUEST),
    SOMEONE_FASTER(7003, "Oops, someone is faster", HttpStatus.BAD_REQUEST),
    RESERVATION_ALREADY_EXISTS(7004, "Reservation already exists", HttpStatus.BAD_REQUEST),;


    private int code;
    private String message;
    private HttpStatus status;
}
