package com.test.paymentsystem.controllers;

import com.test.paymentsystem.models.Booking;
import com.test.paymentsystem.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;


    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Booking> getAllBookings() {
        return bookingService.getBookings();
    }

    @GetMapping
    public List<Booking> getUserBookings(Authentication authentication) {
        String username = authentication.getName();
        return bookingService.getUserBookings(username);
    }

    @PostMapping
    public String createBooking(@RequestBody Booking booking, Authentication authentication) {
        Booking savedBooking = bookingService.createBooking(booking, authentication);

        return savedBooking.getToken();
    }

    @PostMapping("/cancel/{token}")
    public String cancelBookingByToken(@PathVariable String token) {
        return bookingService.cancelBookingByToken(token);
    }
}
