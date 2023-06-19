package com.example.nettalk.service;

import com.example.nettalk.entity.room.Room;
import com.example.nettalk.entity.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    public ResponseEntity list() {
        try {
            List<Room> data = roomRepository.findAll();

            return new ResponseEntity(data, HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    public Room getRoom(Long roomId) {
        try {
            return roomRepository.findById(roomId).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean delete(Room room) {
        try {
            roomRepository.delete(room);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Room save(Room room) {
        return roomRepository.save(room);
    }
}
