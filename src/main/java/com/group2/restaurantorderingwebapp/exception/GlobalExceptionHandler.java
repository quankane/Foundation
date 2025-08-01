package com.group2.restaurantorderingwebapp.exception;

import com.group2.restaurantorderingwebapp.dto.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // handle specific exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest)
    {
        ApiResponse apiResponse = ApiResponse.error(404, exception.getMessage());
        webRequest.getDescription(false);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException exception,WebRequest webRequest){
        ApiResponse apiResponse = ApiResponse.error(exception.getErrorCode().getCode(), exception.getErrorCode().getMessage());
        webRequest.getDescription(false);
        return new ResponseEntity<>(apiResponse,exception.getErrorCode().getStatus());

    }

    // Global exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(Exception exception) {
        ApiResponse apiResponse;

        // TODO: Send stack trace to an observability tool
//        exception.printStackTrace();

        if (exception instanceof BadCredentialsException) {
            apiResponse = ApiResponse.error(401, "The username or password is incorrect");
            return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
        }

        if (exception instanceof AccountStatusException) {
            apiResponse = ApiResponse.error(9999, "The account is locked");
            return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
        }

        if (exception instanceof AccessDeniedException) {
            apiResponse = ApiResponse.error(9999, "You are not authorized to access this resource");
            return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
        }

        if (exception instanceof SignatureException) {
            apiResponse = ApiResponse.error(9999, "The JWT signature is invalid");
            return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
        }

        if (exception instanceof ExpiredJwtException) {
            apiResponse = ApiResponse.error(9999, "The JWT token has expired");
            return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
        }

        if(exception instanceof MalformedJwtException){
            apiResponse = ApiResponse.error(9999, "The JWT token is malformed");
            return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
        }

        // Default handler for unknown exceptions
        apiResponse = ApiResponse.error(500, exception.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException exception, WebRequest webRequest) {
        ApiResponse apiResponse = ApiResponse.error(403, "Access is denied: " + exception.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

}



