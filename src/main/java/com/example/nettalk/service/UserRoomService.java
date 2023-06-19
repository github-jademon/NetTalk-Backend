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

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Transactional
@Service
public class UserRoomService {
    private final UserRoomRepository userRoomRepository;
    private final RoomService roomService;

    public ResponseEntity getRoom(Room room, Member member) {
        try {
            HashMap<String, Object> data = new HashMap<>();
            data.put("room", new Room().builder().comment(room.getComment()).title(room.getTitle()).build());
            if(member!=null) {
                data.put("messages", this.loadUserRoom(member, room));

                Map<String, String> user = new HashMap<>();
                user.put("email", member.getEmail());
                user.put("name", userRoomRepository.findByMemberAndRoom(member, room).get().getUsername());
                if(Objects.equals(member,room.getOwner())) {
                    user.put("owner", "true");
                } else {
                    user.put("owner", "false");
                }

                data.put("user", user);
            }

            return new ResponseEntity(data, HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    public ResponseEntity deleteRoom(Room room, Member member) {
        HashMap<String, Object> data = new HashMap<>();
        try {
            if(member!=null) {
                boolean check = false;
                if(Objects.equals(member,room.getOwner())) {
                    check = roomService.delete(room);
                } else {
                    userRoomRepository.delete(userRoomRepository.findByMemberAndRoom(member, room).get());
                    check = true;
                }
                data.put("messages",check?"success":"fail");
            } else {
                data.put("messages","member == null");
                throw new Exception("member == null");
            }

            return new ResponseEntity(data, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(data, HttpStatus.OK);
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

    public ResponseEntity createRoom(Member owner, Room res, String username) {
        try {
            if(owner==null) throw new Exception();

            LocalDateTime now = LocalDateTime.now();

            List<ChatMessage> chatMessages = new ArrayList<>();

            HashMap<String, Object> data = new HashMap<>();
            Room room = roomService.save(new Room().builder()
                    .owner(owner)
                    .title(res.getTitle())
                    .comment(res.getComment())
                    .build());

            chatMessages.add(new ChatMessage().builder()
                    .username(username)
                    .message(res.getTitle()+" 채팅방이 개설되었습니다.")
                    .type("system")
                    .room(room)
                    .build());

            chatMessages.add(new ChatMessage().builder()
                    .username(username)
                    .message(username+"님이 채팅방에 참여하였습니다.")
                    .type("system")
                    .room(room)
                    .build());

            room.setChatMessages(chatMessages);

            data.put("room", room.getId());

            userRoomRepository.save(
                    new UserRoom()
                            .builder()
                            .room(room)
                            .member(owner)
                            .username(username)
                            .joinAt(now)
                            .build());

            return new ResponseEntity(data, HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void addUserRoom(Member member, Room room, String name) {
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
                if(!message.getDate().isBefore(userRoom.get().getJoinAt())) {
                    chatMessages.add(message);
                }
            }

            return chatMessages;
        }
    }
}
