package com.example.nettalk.entity.room;

import com.example.nettalk.entity.chat.ChatMessage;
import com.example.nettalk.entity.member.Member;
import com.example.nettalk.entity.user_room.UserRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    private Member owner;

    @Column(name = "title")
    @NotBlank(message = "방제목을 입력해주세요.")
    private String title;

    @Column(name = "comment")
    @NotBlank(message = "방 소개를 입력해주세요.")
    private String comment;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<UserRoom> member;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages;

    @Builder
    public Room(Member owner, String title, String comment, List<ChatMessage> chatMessages) {
        this.owner = owner;
        this.title = title;
        this.comment = comment;
        this.chatMessages = chatMessages;
    }

}
