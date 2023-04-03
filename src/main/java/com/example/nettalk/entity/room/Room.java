package com.example.nettalk.entity.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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

    @Column(name = "title")
    @NotBlank(message = "방제목을 입력해주세요.")
    private String title;

    @Column(name = "comment")
    @NotBlank(message = "방 소개를 입력해주세요.")
    private String comment;

    @Column(name = "user_count")
    private String userCount;

    @Column(name = "max_count")
    @NotBlank(message = "최대 인원을 입력해주세요.")
    private String maxCount;

}
