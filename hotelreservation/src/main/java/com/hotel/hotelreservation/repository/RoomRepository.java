
package com.hotel.hotelreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hotel.hotelreservation.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

}

