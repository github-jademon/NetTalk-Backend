package com.example.nettalk.dto;

import com.example.nettalk.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private String email;
    private String userid;

    public static UserResponseDto of(UserEntity user) {
        return new UserResponseDto().builder()
                .email(user.getEmail())
                .userid(user.getUserid())
                .build();
    }
}