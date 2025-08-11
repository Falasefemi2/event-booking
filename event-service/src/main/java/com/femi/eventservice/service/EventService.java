package com.femi.eventservice.service;

import com.femi.eventservice.client.UserServiceClient;
import com.femi.eventservice.dto.ApiResponseData;
import com.femi.eventservice.dto.CreateEventDto;
import com.femi.eventservice.dto.Role;
import com.femi.eventservice.dto.UserDto;
import com.femi.eventservice.exceptions.UnauthorizedException;
import com.femi.eventservice.exceptions.UserNotFoundException;
import com.femi.eventservice.model.Event;
import com.femi.eventservice.model.EventStatus;
import com.femi.eventservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final UserServiceClient userServiceClient;


    @Transactional
    public Event createEvent(CreateEventDto createEventDto, String authToken) {
        ApiResponseData<UserDto> response = userServiceClient.validateToken(authToken);
        UserDto user = response.getData();
        if(user==null){
            throw new UserNotFoundException("User not found or Invalid token");
        }

        if (user.getRole() == null || !Role.ADMIN.equals(user.getRole())) {
            throw new UnauthorizedException("Only administrators can create events");
        }


        Event event = Event.builder()
                .name(createEventDto.getName())
                .description(createEventDto.getDescription())
                .venue(createEventDto.getVenue())
                .eventDateTime(createEventDto.getEventDateTime())
                .totalSeats(createEventDto.getTotalSeats())
                .availableSeats(createEventDto.getTotalSeats())
                .ticketPrice(createEventDto.getTicketPrice())
                .organizerId(user.getId())
                .organizerName(user.getUsername())
                .organizerEmail(user.getEmail())
                .build();
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findByStatus(EventStatus.ACTIVE);
    }

    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public List<Event> getEventsByOrganizer(Long organizerId) {
        return eventRepository.findByOrganizerId(organizerId);
    }

    @Transactional
    public Event updateEvent(Long eventId, CreateEventDto updateDto, String authToken) {
        ApiResponseData<UserDto> response = userServiceClient.validateToken(authToken);
        UserDto user = response.getData();

        if (user.getRole() == null || !Role.ADMIN.equals(user.getRole())) {
            throw new UnauthorizedException("Only administrators can create events");
        }

        Event event = getEventById(eventId);
        if (!event.getOrganizerId().equals(user.getId())) {
            throw new UnauthorizedException("You can only update events you created");
        }

        event.setName(updateDto.getName());
        event.setDescription(updateDto.getDescription());
        event.setVenue(updateDto.getVenue());
        event.setEventDateTime(updateDto.getEventDateTime());
        event.setTicketPrice(updateDto.getTicketPrice());

        return eventRepository.save(event);
    }

    @Transactional
    public void cancelEvent(Long eventId, String authToken) {
        ApiResponseData<UserDto> response = userServiceClient.validateToken(authToken);
        UserDto user = response.getData();

        if (user.getRole() == null || !Role.ADMIN.equals(user.getRole())) {
            throw new UnauthorizedException("Only administrators can create events");
        }

        Event event = getEventById(eventId);

        if (!event.getOrganizerId().equals(user.getId())) {
            throw new UnauthorizedException("You can only cancel events you created");
        }

        event.setStatus(EventStatus.CANCELLED);
        eventRepository.save(event);
    }
}

