package com.femi.eventservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(unique = true, nullable = false)
    private String venue;

    @Column(nullable = false)
    private LocalDateTime eventDateTime;

    @Column(nullable = false)
    private int totalSeats;

    @Column(nullable = false)
    private int availableSeats;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal ticketPrice;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EventStatus status = EventStatus.ACTIVE;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Long organizerId;

    private String organizerName;
    private String organizerEmail;

    public boolean hasAvailableSeats(int requestedSeats) {
        return availableSeats >= requestedSeats;
    }

    public boolean bookSeats(int seatsToBook) {
        if (hasAvailableSeats(seatsToBook)) {
            this.availableSeats -= seatsToBook;
            if (this.availableSeats == 0) {
                this.status = EventStatus.SOLD_OUT;
            }
            return true;
        }
        return false;
    }

    public void releaseSeats(int seatsToRelease) {
        this.availableSeats += seatsToRelease;
        if (this.status == EventStatus.SOLD_OUT && this.availableSeats > 0) {
            this.status = EventStatus.ACTIVE;
        }
    }
}

