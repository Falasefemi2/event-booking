package com.femi.eventservice.controller;

import com.femi.eventservice.dto.CreateEventDto;
import com.femi.eventservice.model.Event;
import com.femi.eventservice.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<Event> createEvent(
            @Valid @RequestBody CreateEventDto createEventDto,
            @RequestHeader("Authorization") String authToken) {
        Event event = eventService.createEvent(createEventDto, authToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody CreateEventDto updateDto,
            @RequestHeader("Authorization") String authToken) {
        Event event = eventService.updateEvent(eventId, updateDto, authToken);
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> cancelEvent(
            @PathVariable Long eventId,
            @RequestHeader("Authorization") String authToken) {
        eventService.cancelEvent(eventId, authToken);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable Long eventId) {
        Event event = eventService.getEventById(eventId);
        return ResponseEntity.ok(event);
    }
}
