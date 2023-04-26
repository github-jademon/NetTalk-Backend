package com.example.nettalk.service;

import com.example.nettalk.entity.chat.ChatMessage;
import com.example.nettalk.entity.member.Member;
import com.example.nettalk.entity.room.Room;
import com.example.nettalk.entity.user_room.UserRoom;
import com.example.nettalk.entity.user_room.UserRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class UserRoomService {
    private final UserRoomRepository userRoomRepository;
    private final RoomService roomService;

    public ResponseEntity getRoom(Room room, Member member) {
        try {
            HashMap<String, Object> data = new HashMap<>();
            data.put("room", new Room().builder().comment(room.getComment()).title(room.getTitle()).owner(room.getOwner()).build());
            if(member!=null) {
                data.put("messages", this.loadUserRoom(member, room));

                Map<String, String> user = new HashMap<>();
                user.put("email", member.getEmail());
                user.put("name", userRoomRepository.findByMemberAndRoom(member, room).get().getUsername());

                data.put("user", user);
            }

            return new ResponseEntity(data, HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

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

    public ResponseEntity createRoom(Member owner, Room res) {
        try {
            if(owner==null) throw new Exception();
            Room room = roomService.save(new Room().builder()
                    .owner(owner)
                    .title(res.getTitle())
                    .comment(res.getComment())
                    .maxCount(res.getMaxCount())
                    .build());

            userRoomRepository.save(
                    new UserRoom()
                            .builder()
                            .room(room)
                            .member(owner)
                            .build());

            return new ResponseEntity(HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void addUserRoom(Member member, Room room, String name) {
        room.setUserCount(room.getUserCount()+1);
        userRoomRepository.save(
                new UserRoom()
                        .builder()
                        .username(name)
                        .room(room)
                        .member(member)
                        .build());
    }

    public void updateUserRoom(Room room, Member member, String username) {
        try {
            UserRoom userRoom = userRoomRepository.findByMemberAndRoom(member, room).get();
            userRoom.setUsername(username);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public List loadUserRoom(Member member, Room room) {
        Optional<UserRoom> userRoom = userRoomRepository.findByMemberAndRoom(member, room);

        if(userRoom.isEmpty()) {
            addUserRoom(member, room, "");
            return null;
        } else {
            List<ChatMessage> chatMessages = new ArrayList<>();

            for(ChatMessage message:room.getChatMessages()) {
                System.out.println(message.getDate()+" "+userRoom.get().getJoinAt());
                if(message.getDate().isAfter(userRoom.get().getJoinAt())) {
                    chatMessages.add(message);
                }
            }

            return chatMessages;
        }
    }
}
