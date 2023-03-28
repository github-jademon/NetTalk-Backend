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
import com.example.nettalk.vo.response.DefaultRes;
import com.example.nettalk.vo.response.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public static final DefaultRes res = new DefaultRes<>(StatusCode.OK, "ok");
    public static HashMap<String, Object> data = new HashMap<>();

    public boolean passwordck(String password, String passwordck) {
        if(password.equals(passwordck)) {
            return false;
        }
        return true;
    }

    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
        try {
            if(memberRepository.findByEmail(memberRequestDto.getEmail()).isPresent()) {
                res.setResponseMessage("이미 가입되어 있는 유저입니다");
                res.setStatusCode(StatusCode.CONFLICT);
                throw new RuntimeException(res.getResponseMessage());
            }
            else if(passwordck(memberRequestDto.getPassword(), memberRequestDto.getPasswordck())) {
                res.setResponseMessage("비밀번호가 다릅니다");
                res.setStatusCode(StatusCode.BAD_REQUEST);
                throw new RuntimeException(res.getResponseMessage());
            }
            res.setStatusCode(StatusCode.OK);
            res.setResponseMessage("회원가입이 완료되었습니다");
            Member user = memberRequestDto.toMember(passwordEncoder);
            return MemberResponseDto.of(memberRepository.save(user));
        } catch(Exception e) {
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
