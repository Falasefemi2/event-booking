package com.femi.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseData<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private List<String> errors;
}