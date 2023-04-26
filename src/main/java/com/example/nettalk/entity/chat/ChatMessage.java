package com.example.nettalk.entity.chat;

import com.example.nettalk.entity.authority.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chats")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String username;
    private String uuid;
    private String message;
    private LocalDateTime date;

    @Builder
    public ChatMessage(String username, String uuid, String type, String message, String date) {
        this.username = username;
        this.uuid = uuid;
        this.type = type;
        this.message = message;
        this.date = LocalDateTime.now();
    }

}
