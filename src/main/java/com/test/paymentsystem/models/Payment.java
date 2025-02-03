package com.test.paymentsystem.models;

import com.test.paymentsystem.enums.PaymentStatus;
import com.test.paymentsystem.enums.Country;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "payments")
@Data
public class Payment {
    @Id
    private String id;
    private String bookingId;
    private String token;
    private PaymentStatus status;
    private String provider;
    private Country country;

    public void setProviderBasedOnCountry() {
        if (Country.RUSSIA == country) {
            this.provider = "FastProvider";
        } else {
            this.provider = "DelayedProvider";
        }
    }
}
