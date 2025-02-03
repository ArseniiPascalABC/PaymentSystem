package com.test.paymentsystem.models;

import com.test.paymentsystem.enums.BookingStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;

@Document(collection = "bookings")
@Data
public class Booking {
    @Id
    private String id;
    private String userId;
    private BookingStatus status;
    private String token;
    private BigDecimal amount;

    public void generateToken() {
        this.token = UUID.randomUUID().toString();
    }
}
