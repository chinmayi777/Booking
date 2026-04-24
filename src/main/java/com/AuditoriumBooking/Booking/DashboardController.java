package com.AuditoriumBooking.Booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        
        long auditoriumCount = bookingRepository.countByVenueType("AUDITORIUM");
        long seminarCount = bookingRepository.countByVenueType("SEMINAR_HALL");
        
        List<Booking> upcomingBookings = bookingRepository.findUpcomingBookings();
        model.addAttribute("auditoriumCount", auditoriumCount);
        model.addAttribute("seminarCount", seminarCount);
        
        // Simple logic for ongoing/upcoming (can be refined based on exact time)
        List<Booking> todayBookings = bookingRepository.findByBookingDate(LocalDate.now());
        model.addAttribute("ongoingCount", todayBookings.size());
        model.addAttribute("upcomingCount", upcomingBookings.size());
        
        model.addAttribute("ongoingEvents", todayBookings);
        
        return "dashboard";
    }
}
