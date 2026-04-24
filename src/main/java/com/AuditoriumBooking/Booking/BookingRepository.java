package com.AuditoriumBooking.Booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookingDate(LocalDate bookingDate);
    
    @Query("SELECT b FROM Booking b WHERE b.bookingDate >= CURRENT_DATE")
    List<Booking> findUpcomingBookings();
    
    long countByVenueType(String venueType);
}
