package com.example.nettalk.service;

import com.example.nettalk.dto.memberRoom.MemberRoomUpdateRequestDto;
import com.example.nettalk.entity.chat.ChatMessage;
import com.example.nettalk.entity.member.Member;
import com.example.nettalk.entity.member.MemberRepository;
import com.example.nettalk.entity.room.Room;
import com.example.nettalk.entity.room.RoomRepository;
import com.example.nettalk.entity.user_room.UserRoom;
import com.example.nettalk.entity.user_room.UserRoomRepository;
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

    public Room save(Room room) {
        return roomRepository.save(room);
    }
}
