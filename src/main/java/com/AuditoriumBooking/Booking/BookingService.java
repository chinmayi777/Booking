package com.AuditoriumBooking.Booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public long countAuditoriums() {
        return bookingRepository.countByVenueType("AUDITORIUM");
    }

    public long countSeminarHalls() {
        return bookingRepository.countByVenueType("SEMINAR_HALL");
    }

    public List<Booking> getUpcomingBookings() {
        return bookingRepository.findUpcomingBookings();
    }

    public List<Booking> getTodayBookings() {
        return bookingRepository.findByBookingDate(LocalDate.now());
    }

    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}
