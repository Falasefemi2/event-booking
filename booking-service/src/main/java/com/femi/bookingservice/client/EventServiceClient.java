package com.femi.bookingservice.client;

import com.femi.bookingservice.dto.EventDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "event-service", url = "${event-service.url}")
public interface EventServiceClient {

    @GetMapping("/api/events/{eventId}")
    ResponseEntity<EventDto> getEventById(@PathVariable("eventId") Long eventId);

}
