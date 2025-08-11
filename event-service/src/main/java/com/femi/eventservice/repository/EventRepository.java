package com.femi.eventservice.repository;

import com.femi.eventservice.model.Event;
import com.femi.eventservice.model.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByStatus(EventStatus status);
    List<Event> findByOrganizerId(Long organizerId);
    List<Event> findByVenueContainingIgnoreCase(String venue);
}
