
package com.hotel.hotelreservation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hotel.hotelreservation.entity.Room;
import com.hotel.hotelreservation.repository.RoomRepository;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room addRoom(Room room) {
        return roomRepository.save(room);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

   
public Room getRoomById(Long id) {

    java.util.Optional<Room> optionalRoom = roomRepository.findById(id);

    if (optionalRoom.isPresent()) {
        return optionalRoom.get();
    } else {
        throw new RuntimeException("Room not found with ID: " + id);
    }
}

    

    public Room updateRoom(Long id, Room updatedRoom) {
        Room existingRoom = getRoomById(id);

        existingRoom.setRoomNumber(updatedRoom.getRoomNumber());
        existingRoom.setCategory(updatedRoom.getCategory());
        existingRoom.setPricePerNight(updatedRoom.getPricePerNight());
        existingRoom.setAvailable(updatedRoom.isAvailable());

        return roomRepository.save(existingRoom);
    }

    public String deleteRoom(Long id) {
    Room room = roomRepository.findById(id)
            .orElseThrow(() ->
                    new RuntimeException("Room not found"));

    room.setAvailable(false);
    roomRepository.save(room);

    return "Room marked as unavailable successfully";
}
}

