
package com.hotel.hotelreservation.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hotel.hotelreservation.entity.Booking;
import com.hotel.hotelreservation.entity.Room;
import com.hotel.hotelreservation.repository.BookingRepository;
import com.hotel.hotelreservation.repository.RoomRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    public BookingService(BookingRepository bookingRepository,
                          RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    public Booking createBooking(Booking booking) {

        Optional<Room> optionalRoom =
                roomRepository.findById(booking.getRoom().getId());

        if (!optionalRoom.isPresent()) {
            throw new RuntimeException("Room not found");
        }

       Room room = optionalRoom.get();

      if (!room.isAvailable()) {
    throw new RuntimeException("Room is not available for booking");
}

          LocalDate checkIn = booking.getCheckInDate();
          LocalDate checkOut = booking.getCheckOutDate();

        

        if (checkIn == null || checkOut == null) {
            throw new RuntimeException("Check-in and check-out dates are required");
        }
       

        if (!checkOut.isAfter(checkIn)) {
    throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Check-out date must be after check-in date"
    );
}

        if (checkIn.isBefore(LocalDate.now())) {
    throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Check-in cannot be in the past"
    );
    }

       
        boolean alreadyBooked =
        bookingRepository
        .existsByRoomIdAndStatusNotAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                room.getId(),
                "CANCELLED",
                checkOut,
                checkIn
        );

if (alreadyBooked) {
    throw new RuntimeException(
            "Room is already booked for these dates"
    );
}

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);

        booking.setRoom(room);
        booking.setTotalAmount(nights * room.getPricePerNight());
        booking.setStatus("CONFIRMED");
        booking.setPaymentStatus("PENDING");

        

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {

        Optional<Booking> optionalBooking =
                bookingRepository.findById(id);

        if (optionalBooking.isPresent()) {
            return optionalBooking.get();
        } else {
            throw new RuntimeException("Booking not found with ID: " + id);
        }
    }

    public Booking cancelBooking(Long id) {

        Booking booking = getBookingById(id);

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException("Booking is already cancelled");
        }

        booking.setStatus("CANCELLED");

        Room room = booking.getRoom();
        

        return bookingRepository.save(booking);

    }
    public Booking makePayment(Long id) {

    Booking booking = getBookingById(id);

    if ("CANCELLED".equals(booking.getStatus())) {
    throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "Cannot pay for a cancelled booking"
    );
}

    if ("PAID".equals(booking.getPaymentStatus())) {
        throw new RuntimeException(
            "Payment has already been completed"
        );
    }

    booking.setPaymentStatus("PAID");

    return bookingRepository.save(booking);
}
}

