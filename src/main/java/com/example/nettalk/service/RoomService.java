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
    private final MemberRepository memberRepository;
    private final UserRoomRepository userRoomRepository;

    public ResponseEntity list() {
        try {
            List<Room> data = roomRepository.findAll();

            return new ResponseEntity(data, HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    public ResponseEntity getRoom(Long userId, Long roomId) {
        try {
            HashMap<String, Object> data = new HashMap<>();
            Room room = roomRepository.findById(roomId).get();
            data.put("room", new Room().builder().comment(room.getComment()).title(room.getTitle()).owner(room.getOwner()).build());
            if(userId!=null) {
                data.put("messages", this.update(userId, roomId));
                Member member = memberRepository.findById(userId).get();
                String username = userRoomRepository.findByMemberAndRoom(member, room).get().getUsername();
                Map<String, String> user = new HashMap<>();
                user.put("email", member.getEmail());
                user.put("name", username);
                data.put("user", user);
            }

            return new ResponseEntity(data, HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    public ResponseEntity createRoom(Long userid, Room res) {
        try {
            if(userid==null) throw new Exception();
            Member owner = memberRepository.findById(userid).get();
            Room room = roomRepository.save(new Room().builder()
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

    public void updateUserRoom(Long id, MemberRoomUpdateRequestDto memberRoomUpdateRequestDto) {
        try {
            System.out.println(memberRoomUpdateRequestDto.getEmail());

            Member member = memberRepository.findByEmail(memberRoomUpdateRequestDto.getEmail()).get();
            Room room = roomRepository.findById(id).get();
            UserRoom userRoom = userRoomRepository.findByMemberAndRoom(member, room).get();

            System.out.println(memberRoomUpdateRequestDto.getName());

            userRoom.setUsername(memberRoomUpdateRequestDto.getName());
        } catch(Exception e) {
            e.printStackTrace();
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

    public List update(Long userId, Long roomId) {
        Member member = memberRepository.findById(userId).get();
        Room room = roomRepository.findById(roomId).orElse(null);
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
