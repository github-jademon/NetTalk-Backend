package com.example.nettalk.service;

import com.example.nettalk.dto.token.TokenDto;
import com.example.nettalk.dto.token.TokenRequestDto;
import com.example.nettalk.dto.member.MemberRequestDto;
import com.example.nettalk.dto.member.MemberResponseDto;
import com.example.nettalk.entity.token.RefreshToken;
import com.example.nettalk.entity.member.Member;
import com.example.nettalk.jwt.TokenProvider;
import com.example.nettalk.entity.token.RefreshTokenRepository;
import com.example.nettalk.entity.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    public final static HashMap<String, Object> data = new HashMap<>();

    public boolean passwordck(String password, String passwordck) {
        if(password.equals(passwordck)) {
            return true;
        }
        return false;
    }

    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
        String message = "";
        try {
            System.out.println("1");
            if(memberRepository.findByEmail(memberRequestDto.getEmail()).isPresent()) {
                message = "이미 가입되어 있는 유저입니다";
                throw new RuntimeException(message);
            }
            System.out.println("2");

            if(passwordck(memberRequestDto.getPassword(), memberRequestDto.getPassword())) {
                message = "비밀번호가 다릅니다";
                throw new RuntimeException(message);
            }

            Member user = memberRequestDto.toMember(passwordEncoder);
            return MemberResponseDto.of(memberRepository.save(user));
        } catch(Exception e) {
            if(!message.equals("")) {
                AuthService.data.put("message", message);
            }
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    public TokenDto login(MemberRequestDto memberRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

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
