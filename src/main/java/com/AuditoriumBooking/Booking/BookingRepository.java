package com.AuditoriumBooking.Booking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<User,Long> {
}
