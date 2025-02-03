package com.test.paymentsystem.repositories;

import com.test.paymentsystem.models.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

    Payment findByToken(String token);
}
