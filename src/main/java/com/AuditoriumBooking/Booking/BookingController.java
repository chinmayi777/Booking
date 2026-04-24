package com.AuditoriumBooking.Booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/bookings/create")
    public String createBooking(
            @RequestParam String date,
            @RequestParam String venueType,
            @RequestParam String eventName,
            @RequestParam String personName,
            @RequestParam String department,
            @RequestParam String semester,
            @RequestParam String startTime,
            @RequestParam String endTime) {
            
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        Booking booking = new Booking();
        if (date != null && !date.isEmpty()) {
            booking.setBookingDate(LocalDate.parse(date));
        } else {
            booking.setBookingDate(LocalDate.now()); // Fallback
        }
        booking.setVenueType(venueType);
        booking.setEventName(eventName);
        booking.setPersonName(personName);
        booking.setDepartment(department);
        booking.setSemester(semester);
        booking.setStartTime(LocalTime.parse(startTime));
        booking.setEndTime(LocalTime.parse(endTime));
        booking.setUser(user);
        
        bookingRepository.save(booking);

        return "redirect:/dashboard?success=true";
    }
}
