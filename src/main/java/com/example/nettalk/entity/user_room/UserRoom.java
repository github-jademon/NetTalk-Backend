package com.example.nettalk.entity.user_room;

import com.example.nettalk.entity.member.Member;
import com.example.nettalk.entity.room.Room;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Room room;

    @ManyToOne
    @JsonIgnore
    private Member member;

    private String username;

    private LocalDateTime joinAt;

    @Builder
    public UserRoom(Room room, Member member, String username, LocalDateTime joinAt) {
        this.room = room;
        this.member = member;
        this.username = username;
        this.joinAt = joinAt;
    }

    @Builder
    public UserRoom(Room room, Member member, String username) {
        this.room = room;
        this.member = member;
        this.username = username;
        this.joinAt = LocalDateTime.now();
    }

}
