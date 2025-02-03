package com.test.paymentsystem.repositories;

import com.test.paymentsystem.enums.BookingStatus;
import com.test.paymentsystem.models.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findAllByUserId(String userId);

    List<Booking> findAllByStatus(BookingStatus status);

    void deleteBookingByToken(String token);
}
