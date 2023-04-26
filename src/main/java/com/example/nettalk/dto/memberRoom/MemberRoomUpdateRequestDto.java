package com.example.nettalk.dto.memberRoom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRoomUpdateRequestDto {
    private String email;
    private String name;
}
