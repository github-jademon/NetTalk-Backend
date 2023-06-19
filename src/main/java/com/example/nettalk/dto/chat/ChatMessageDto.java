package com.example.nettalk.dto.chat;

import com.example.nettalk.entity.chat.ChatMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {

    private Long roomId;
    private String type;
    private String username;
    private String uuid;
    private String message;
    private String date;

    public ChatMessage toChatMessage() {
        return ChatMessage.builder()
                .username(username)
                .uuid(uuid)
                .message(message)
                .date(date)
                .build();
    }
}