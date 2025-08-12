package com.femi.bookingservice.repository;

import com.femi.bookingservice.model.Book;
import com.femi.bookingservice.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Book, Long> {
    List<Book> findByUserId(Long userId);
    List<Book> findByEventId(Long eventId);
    List<Book> findByStatus(BookingStatus status);
    Optional<Book> findByUserIdAndEventId(Long userId, Long eventId);
    List<Book> findByUserIdAndStatus(Long userId, BookingStatus status);
    List<Book> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    @Query("SELECT COALESCE(SUM(b.numberOfTickets), 0) FROM Book b WHERE b.eventId = :eventId AND b.status IN :statuses")
    Integer countTicketsByEventIdAndStatusIn(@Param("eventId") Long eventId, @Param("statuses") List<BookingStatus> statuses);
    boolean existsByUserIdAndEventIdAndStatusIn(Long userId, Long eventId, List<BookingStatus> statuses);
    @Query("SELECT b FROM Book b WHERE b.status = 'PENDING' AND b.createdAt < :cutoffTime")
    List<Book> findExpiredPendingBookings(@Param("cutoffTime") LocalDateTime cutoffTime);
}
