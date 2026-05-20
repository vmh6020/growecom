package com.kevin.growecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private ResponseStatusDTO status;
    private T data;
    private Instant timeStamp;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class ResponseStatusDTO {
        private HttpStatus statusCode;
        private String message;
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return ResponseEntity.ok(ApiResponse.<T>builder()
                        .status(ResponseStatusDTO.builder()
                                .statusCode(HttpStatus.OK)
                                .message(message).build())
                        .data(data)
                        .timeStamp(Instant.now())
                        .build());
    }
    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<T>builder()
                .status(ResponseStatusDTO.builder()
                        .statusCode(HttpStatus.CREATED)
                        .message(message)
                        .build())
                .data(data)
                .timeStamp(Instant.now())
                .build());
    }


}