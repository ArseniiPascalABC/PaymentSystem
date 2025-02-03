package com.test.paymentsystem.services;

import com.test.paymentsystem.enums.Country;
import com.test.paymentsystem.enums.PaymentStatus;
import com.test.paymentsystem.models.Booking;
import com.test.paymentsystem.models.Payment;
import com.test.paymentsystem.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public void createPayment(Booking booking) {
        Payment payment = new Payment();
        payment.setBookingId(booking.getId());
        payment.setToken(booking.getToken());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCountry(Country.OTHER);
        payment.setProviderBasedOnCountry();
        paymentRepository.save(payment);
    }

    public Payment processPayment(String token) {
        Payment payment = paymentRepository.findByToken(token);

        if ("DelayedProvider".equals(payment.getProvider())) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                payment.setStatus(PaymentStatus.FAILED);
                log.error("PaymentService: processPayment. We got error, error is:\n{1}", e);
            }
        }
        payment.setStatus(PaymentStatus.SUCCESS);

        return paymentRepository.save(payment);
    }

    public List<Payment> getPayments() {
        return paymentRepository.findAll();
    }

}
