package com.femi.bookingservice.client;

import com.femi.bookingservice.dto.ApiResponseData;
import com.femi.bookingservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "${user-service.url}")
public interface UserServiceClient {

    @GetMapping("/api/users/{id}")
    ResponseEntity<ApiResponseData<UserDto>> getUserById(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String authToken
    );
}
