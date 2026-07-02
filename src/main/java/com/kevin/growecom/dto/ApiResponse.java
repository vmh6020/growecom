package com.kevin.growecom.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.micrometer.observation.autoconfigure.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;
    private Instant timeStamp;

    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return ApiResponse.buildResponse(HttpStatus.OK, message, data);
    }
    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return ApiResponse.buildResponse(HttpStatus.CREATED, message, data);
    }
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message) {
        return ApiResponse.buildResponse(status, message, null);
    }
    public static <T> ResponseEntity<ApiResponse<T>> buildResponse(HttpStatus status, String message, T data) {
        return ResponseEntity.status(status).body(ApiResponse.<T>builder()
                .statusCode(status.value())
                .message(message)
                .data(data)
                .timeStamp(Instant.now())
                .build());
    }

}