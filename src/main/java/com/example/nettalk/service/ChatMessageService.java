package com.example.nettalk.service;

import com.example.nettalk.entity.chat.ChatMessage;
import com.example.nettalk.entity.chat.ChatMessageRepository;
import com.example.nettalk.entity.room.Room;
import com.example.nettalk.entity.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public void save(Long roomId, ChatMessage chatMessage, String type) {
        Optional<Room> roomOpt = roomRepository.findById(roomId);

        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            ChatMessage message = ChatMessage.builder()
                    .uuid(chatMessage.getUuid())
                    .username(chatMessage.getUsername())
                    .type(type)
                    .message(chatMessage.getMessage())
                    .room(room)
                    .build();

            chatMessageRepository.save(message);

            room.getChatMessages().add(message);
        }
    }

}
