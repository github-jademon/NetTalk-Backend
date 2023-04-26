package com.example.nettalk.service;

import com.example.nettalk.dto.memberRoom.MemberRoomUpdateRequestDto;
import com.example.nettalk.entity.member.Member;
import com.example.nettalk.entity.room.Room;
import com.example.nettalk.entity.user_room.UserRoom;
import com.example.nettalk.entity.user_room.UserRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserRoomService {
    private final UserRoomRepository userRoomRepository;

    public ResponseEntity mypage(Member member) {
        try {
            List<UserRoom> userRooms = userRoomRepository.findAllByMember(member);
            List<Room> data = new ArrayList<>();

            for(UserRoom room:userRooms) {
                data.add(room.getRoom());
            }

            return new ResponseEntity(data, HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    public void updateUserRoom(Room room, Member member, String username) {
        try {
            UserRoom userRoom = userRoomRepository.findByMemberAndRoom(member, room).get();
            userRoom.setUsername(username);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
