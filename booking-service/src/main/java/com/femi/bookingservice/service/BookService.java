package com.femi.bookingservice.service;

import com.femi.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookingRepository bookingRepository;
}
