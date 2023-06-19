package com.example.nettalk.dto.room;

import com.example.nettalk.entity.room.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomRequestDto {
    private Room room;
    private String username;
}
