package com.example.nettalk.service;

import com.example.nettalk.dto.TokenDto;
import com.example.nettalk.dto.TokenRequestDto;
import com.example.nettalk.dto.UserRequestDto;
import com.example.nettalk.dto.UserResponseDto;
import com.example.nettalk.entity.RefreshToken;
import com.example.nettalk.entity.UserEntity;
import com.example.nettalk.jwt.TokenProvider;
import com.example.nettalk.repository.RefreshTokenRepository;
import com.example.nettalk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public UserResponseDto signup(UserRequestDto userRequestDto) {
        if(userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        UserEntity user = userRequestDto.toMember(passwordEncoder);
        return UserResponseDto.of(userRepository.save(user));
    }

    @Transactional
    public TokenDto login(UserRequestDto userRequestDto) {
        System.out.println("1111");

        UsernamePasswordAuthenticationToken authenticationToken = userRequestDto.toAuthentication();

        System.out.println("2222");
        System.out.println(authenticationToken);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        System.out.println("3333");
        System.out.println(authentication);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        System.out.println("4444");
        System.out.println(tokenDto);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        System.out.println(refreshToken);

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        if(!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        if(!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }
}
