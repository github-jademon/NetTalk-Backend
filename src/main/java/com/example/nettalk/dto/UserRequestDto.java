package com.example.nettalk.dto;

import com.example.nettalk.entity.Authority;
import com.example.nettalk.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    private String email;
    private String password;
    private String userid;

    public UserEntity toMember(PasswordEncoder passwordEncoder) {
        return UserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .userid(userid)
                .authority(Authority.ROLE_USER)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}