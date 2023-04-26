package com.example.nettalk.entity.member;

import com.example.nettalk.entity.authority.Authority;
import javax.persistence.*;

import com.example.nettalk.entity.room.Room;
import com.example.nettalk.entity.user_room.UserRoom;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="users")
public class Member {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "email")
    private String email;
    @Column(nullable = false, name = "userid")
    private String userid;
    @Column(nullable = false, name = "password")
    private String password;
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @OneToMany(mappedBy = "member")
    private List<UserRoom> room;

    @Builder
    public Member(String email, String userid, String password, Authority authority, List room) {
        this.email = email;
        this.userid = userid;
        this.password = password;
        this.authority = authority;
        this.room = room;
    }
}
