package com.kevin.growecom.util.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "Email already exists in the system"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Refresh token has expired"),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Refresh token not found"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested resource not found"),
    INSUFFICIENT_STOCK(HttpStatus.UNPROCESSABLE_CONTENT, "Insufficient stock for this product"),
    NAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "Name already exists"),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "You do not have permission to access this resource"),
    CART_IS_EMPTY(HttpStatus.BAD_REQUEST, "Your cart is empty");

    private final HttpStatus statusCode;
    private final String message;

    ErrorCode(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
