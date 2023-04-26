package com.example.nettalk.service;

import com.example.nettalk.entity.chat.ChatMessage;
import com.example.nettalk.entity.chat.ChatMessageRepository;
import com.example.nettalk.entity.room.Room;
import com.example.nettalk.entity.room.RoomRepository;
import com.example.nettalk.entity.user_room.UserRoom;
import com.example.nettalk.entity.user_room.UserRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final RoomRepository roomRepository;

    public void save(Long roomId, ChatMessage chatMessage, String type) {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isPresent()) {
            room.get().getChatMessages().add(
                    chatMessageRepository.save(ChatMessage.builder()
                            .uuid(chatMessage.getUuid())
                            .username(chatMessage.getUsername())
                            .type(type)
                            .message(chatMessage.getMessage())
                            .build()));
        }
    }

}
