package com.test.paymentsystem.services;

import com.test.paymentsystem.enums.BookingStatus;
import com.test.paymentsystem.enums.PaymentStatus;
import com.test.paymentsystem.models.Booking;
import com.test.paymentsystem.models.Payment;
import com.test.paymentsystem.repositories.BookingRepository;
import com.test.paymentsystem.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;

    public List<Booking> getBookings() {
        return bookingRepository.findAll();
    }

    public Booking createBooking(Booking booking, Authentication authentication) {
        String username = authentication.getName();
        booking.setUserId(username);
        booking.setStatus(BookingStatus.NEW);
        booking.generateToken();
        Booking savedBooking = bookingRepository.save(booking);

        paymentService.createPayment(savedBooking);
        return savedBooking;
    }

    public String cancelBookingByToken(String token) {
        Payment payment = paymentRepository.findByToken(token);
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return "You cannot cancel the booking after payment.";
        }
        payment.setStatus(PaymentStatus.FAILED);
        bookingRepository.deleteBookingByToken(token);
        return token + " was cancelled";
    }

    public List<Booking> getUserBookings(String userId) {
        return bookingRepository.findAllByUserId(userId);
    }

    @Scheduled(cron = "${paymentService.updatePaymentStatuses.cron}")
    public void updateBookingStatuses() {
        log.info("BookingService: updateBookingStatuses. Scheduler started");
        List<Booking> unpaidBookings = bookingRepository.findAllByStatus(BookingStatus.NEW);
        for (Booking booking : unpaidBookings) {
            Payment payment = paymentRepository.findByToken(booking.getToken());

            if (payment.getStatus() == PaymentStatus.SUCCESS) {
                booking.setStatus(BookingStatus.PAID);
            } else if (payment.getStatus() == PaymentStatus.FAILED) {
                booking.setStatus(BookingStatus.CANCELED);
            }

            bookingRepository.save(booking);
        }
        log.info("BookingService: updateBookingStatuses. Scheduler has finished");
    }
}
