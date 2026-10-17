
package com.hotel.hotelreservation.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hotel.hotelreservation.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> 
{
   boolean existsByRoomIdAndStatusNotAndCheckInDateLessThanAndCheckOutDateGreaterThan(
    Long roomId,
    String status,
    LocalDate checkOutDate,
    LocalDate checkInDate
);

}
