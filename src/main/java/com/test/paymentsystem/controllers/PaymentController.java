package com.test.paymentsystem.controllers;

import com.test.paymentsystem.models.Payment;
import com.test.paymentsystem.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Payment> getAllBookings() {
        return paymentService.getPayments();
    }

    @PostMapping
    public Payment processPayment(@RequestParam String token) {
        return paymentService.processPayment(token);
    }

}
