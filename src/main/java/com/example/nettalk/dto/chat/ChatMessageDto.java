package com.example.nettalk.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {

    private String roomId;
    private String type;
    private String name;
    private String uuid;
    private String message;
    private String date;
}