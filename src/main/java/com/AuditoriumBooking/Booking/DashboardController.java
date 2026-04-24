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
        
        List<Booking> myBookings = bookingRepository.findByUser(user);
        model.addAttribute("myBookings", myBookings);
        
        // Prepare bookings JSON for calendar
        List<Booking> allBookings = bookingRepository.findAll();
        java.util.Map<String, List<java.util.Map<String, Object>>> bookingsMap = new java.util.HashMap<>();
        for (Booking b : allBookings) {
            if (b.getBookingDate() == null) continue;
            String date = b.getBookingDate().toString();
            bookingsMap.putIfAbsent(date, new java.util.ArrayList<>());
            
            java.util.Map<String, Object> dto = new java.util.HashMap<>();
            dto.put("eventName", b.getEventName());
            dto.put("venueType", b.getVenueType());
            dto.put("department", b.getDepartment());
            dto.put("personName", b.getPersonName());
            dto.put("startTime", b.getStartTime() != null ? b.getStartTime().toString() : "");
            dto.put("endTime", b.getEndTime() != null ? b.getEndTime().toString() : "");
            
            bookingsMap.get(date).add(dto);
        }
        
        StringBuilder jsonBuilder = new StringBuilder("{");
        boolean firstDate = true;
        for (java.util.Map.Entry<String, List<java.util.Map<String, Object>>> entry : bookingsMap.entrySet()) {
            if (!firstDate) jsonBuilder.append(",");
            jsonBuilder.append("\"").append(entry.getKey()).append("\":[");
            
            boolean firstBooking = true;
            for (java.util.Map<String, Object> b : entry.getValue()) {
                if (!firstBooking) jsonBuilder.append(",");
                jsonBuilder.append("{")
                           .append("\"eventName\":\"").append(escapeJson((String)b.get("eventName"))).append("\",")
                           .append("\"venueType\":\"").append(b.get("venueType")).append("\",")
                           .append("\"department\":\"").append(b.get("department")).append("\",")
                           .append("\"personName\":\"").append(escapeJson((String)b.get("personName"))).append("\",")
                           .append("\"startTime\":\"").append(b.get("startTime")).append("\",")
                           .append("\"endTime\":\"").append(b.get("endTime")).append("\"")
                           .append("}");
                firstBooking = false;
            }
            jsonBuilder.append("]");
            firstDate = false;
        }
        jsonBuilder.append("}");
        
        model.addAttribute("bookingsJson", jsonBuilder.toString());
        
        return "dashboard";
    }
    
    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
