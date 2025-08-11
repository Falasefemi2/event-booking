package com.femi.eventservice.client;

import com.femi.eventservice.dto.ApiResponseData;
import com.femi.eventservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "${user-service.url}")
public interface UserServiceClient {

    @GetMapping("/users/{id}")
    UserDto getUserById(@PathVariable("id") Long userId,
                        @RequestHeader("Authorization") String token);

    @GetMapping("/auth/validate")
    ApiResponseData<UserDto> validateToken(@RequestHeader("Authorization") String token);
}
